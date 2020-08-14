package com.wangk.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangk.core.MyPageBean;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import com.wangk.model.HtmlType;
import com.wangk.model.LchHtmlInfo;
import com.wangk.service.HtmlTypeService;
import com.wangk.service.LchService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName :LchController
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 9:55
 * @Version :1.0
 **/
@RestController
@RequestMapping("/lch")
public class LchController {

    @Autowired
    LchService service;
    @Autowired
    HtmlTypeService htmlTypeService;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/getUrlByTypeId")
    public Result getUrlByTypeId(@RequestParam String typeId) {
        List<LchHtmlInfo> lchHtmlInfos=new ArrayList<>();
        if (StringUtils.isNotBlank(typeId)){
            QueryWrapper<LchHtmlInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("typeId",typeId);
            lchHtmlInfos = service.list(queryWrapper);
        }else {
            lchHtmlInfos = service.list();
        }
        return ResultGenerator.getSuccessInfo(lchHtmlInfos);
    }

    @RequestMapping("/page")
    public Result getAllUrl(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,String name) {
        MyPageBean pageBean = service.getUrlFromES(page, size, name);
        return ResultGenerator.getSuccessInfo(pageBean);
    }

    @RequestMapping("/import")
    public Result importData(MultipartFile file) {
        int result = service.importData(file);
        return ResultGenerator.getSuccessInfo(result);
    }

    @PostMapping("/")
    public Result insert(@RequestBody LchHtmlInfo lchHtmlInfo){
        boolean save = service.save(lchHtmlInfo);
        if (save){
            return ResultGenerator.getSuccessInfo();
        }
        return  ResultGenerator.getFailureInfo("添加失败");
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        if (service.removeById(id)) {
            return ResultGenerator.getSuccessInfo();
        }
        return ResultGenerator.getFailureInfo("删除失败");
    }
    @RequestMapping("/getType")
    public Result getHtmlType(){
        List<HtmlType> htmlTypes = htmlTypeService.list();
        return ResultGenerator.getSuccessInfo(htmlTypes);
    }

    @RequestMapping("/delAll")
    public Result delAll(){
        boolean remove = service.remove(null);
        if (remove){
            return ResultGenerator.getSuccessInfo();
        }
        return ResultGenerator.getFailureInfo("操作失败，请联系管理员");
    }
    @RequestMapping("/delBatch")
    public Result delBatch(@RequestParam("Ids") String Ids){
        //根据id批量删除ES中的数据
        String[] split = Ids.split(",");
        Integer[] convert = Convert.convert( Integer[].class, split);
        List<Integer> ids = Arrays.asList(convert);
        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setQuery(QueryBuilders.termsQuery("id",ids));
        this.elasticsearchTemplate.delete(deleteQuery,LchHtmlInfo.class);

        QueryWrapper<LchHtmlInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        boolean remove = service.remove(queryWrapper);
        if (remove){
            return ResultGenerator.getSuccessInfo();
        }
        return ResultGenerator.getFailureInfo("操作失败，请联系管理员");
    }
}
