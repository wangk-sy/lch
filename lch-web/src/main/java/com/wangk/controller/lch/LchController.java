package com.wangk.controller.lch;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangk.core.MyPageBean;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import com.wangk.model.HtmlType;
import com.wangk.model.LchHtmlInfo;
import com.wangk.service.HtmlTypeService;
import com.wangk.service.LchService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public Result getAllUrl(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        IPage<LchHtmlInfo> iPage = new Page<>(page, size);
        IPage<LchHtmlInfo> pageInfo = service.page(iPage);
        MyPageBean pageBean = new MyPageBean();
        pageBean.setData(pageInfo.getRecords());
        pageBean.setTotal(pageInfo.getTotal());
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
        String[] split = Ids.split(",");
        List<String> idList = Arrays.asList(split);
        QueryWrapper<LchHtmlInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",idList);
        boolean remove = service.remove(queryWrapper);
        if (remove){
            return ResultGenerator.getSuccessInfo();
        }
        return ResultGenerator.getFailureInfo("操作失败，请联系管理员");
    }
}
