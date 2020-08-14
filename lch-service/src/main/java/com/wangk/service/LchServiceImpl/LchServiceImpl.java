package com.wangk.service.LchServiceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangk.core.MyPageBean;
import com.wangk.mapper.HtmlTypeMapper;
import com.wangk.mapper.LchMapper;
import com.wangk.model.HtmlType;
import com.wangk.model.LchHtmlInfo;
import com.wangk.service.LchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @ClassName :LchServiceImpl
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/11 16:16
 * @Version :1.0
 **/
@Service
public class LchServiceImpl implements LchService {

    @Autowired
    LchMapper mapper;
    @Autowired
    HtmlTypeMapper htmlTypeMapper;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public int importData(MultipartFile file) {
        Workbook workbook=null;
        try {
            workbook=new XSSFWorkbook(file.getInputStream());
            //获取sheet数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i <numberOfSheets ; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //获取行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j <physicalNumberOfRows ; j++) {
                    if (j == 0) {
                        continue;//跳过标题行
                    }
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    LchHtmlInfo lchHtmlInfo = new LchHtmlInfo();
                    QueryWrapper<HtmlType> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("name",sheet.getSheetName());
                    HtmlType htmlType = htmlTypeMapper.selectOne(queryWrapper);
                    if (htmlType==null){
                        htmlType=new HtmlType();
                        htmlType.setName(sheet.getSheetName());
                        htmlTypeMapper.insert(htmlType);
                    }
                    lchHtmlInfo.setTypeId(htmlType.getId());
                    //获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    for (int k = 0; k <physicalNumberOfCells ; k++) {
                        Cell cell = row.getCell(k);
                        switch (k) {
                            case 1:
                                lchHtmlInfo.setName(cell.getStringCellValue());
                                break;
                            case 2:
                                lchHtmlInfo.setUrl(cell.getStringCellValue());
                                break;
                            default :
                        }
                    }
                    mapper.insert(lchHtmlInfo);
                    IndexQuery indexQuery = new IndexQueryBuilder().withObject(lchHtmlInfo).build();
                    elasticsearchTemplate.index(indexQuery);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public MyPageBean getUrlFromES(Integer page, Integer size, String name) {
        PageRequest pageRequest = PageRequest.of(page, size);
        NativeSearchQuery searchQuery;
        AggregatedPage<LchHtmlInfo> lchHtmlInfos;
        if(StringUtils.isNotBlank(name)){
            searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("name",name))
                    .withPageable(pageRequest)
                    .withHighlightFields(new HighlightBuilder.Field("name"))
                    .build();
            lchHtmlInfos = elasticsearchTemplate.queryForPage(searchQuery, LchHtmlInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                    List<T> list = new ArrayList<T>();
                    if (response.getHits().totalHits==0){
                        return new AggregatedPageImpl<>(Collections.emptyList(),pageable,0);
                    }
                    for(SearchHit searchHit:response.getHits()) {
                        //通过反射写入到对象中
                        T obj = (T) ReflectUtils.newInstance(clazz);

                        //将命中的数据映射到对象obj中
                        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                        for (Map.Entry<String,Object> entry:sourceAsMap.entrySet()) {
                            Field field = FieldUtils.getField(clazz, entry.getKey(), true);
                            if (field==null){
                                continue;
                            }
                            try {
                                FieldUtils.writeField(obj,entry.getKey(),entry.getValue(), true);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        //处理高亮字段
                        for (Map.Entry<String, HighlightField> entry:searchHit.getHighlightFields().entrySet()) {
                            Text[] fragments = entry.getValue().getFragments();
                            StringBuilder sb = new StringBuilder();
                            for (Text fragment:fragments) {
                                sb.append(fragment.toString());
                            }
                            try {
                                FieldUtils.writeField(obj,entry.getKey(),sb.toString(), true);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        list.add(obj);
                    }
                    return new AggregatedPageImpl<>(list,pageable,response.getHits().totalHits);
                }
                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> type) {
                    return null;
                }
            });
        }else {
            searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).withPageable(pageRequest).build();
            lchHtmlInfos = elasticsearchTemplate.queryForPage(searchQuery, LchHtmlInfo.class);
        }
        MyPageBean pageBean = new MyPageBean();
        pageBean.setData(lchHtmlInfos.getContent());
        pageBean.setTotal(lchHtmlInfos.getTotalElements());
        return pageBean;
    }

    @Override
    public boolean save(LchHtmlInfo entity) {
        int result = mapper.insert(entity);
        if (result==0){
            return false;
        }
            return true;
    }

    @Override
    public boolean saveBatch(Collection<LchHtmlInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<LchHtmlInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean removeById(Serializable id) {
        //数据库删除的同时，需要删除es中的数据
        this.elasticsearchTemplate.delete(LchHtmlInfo.class,id.toString());
        int result = mapper.deleteById(id);
        if (result==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return false;
    }

    @Override
    public boolean remove(Wrapper<LchHtmlInfo> queryWrapper) {

        int delete = mapper.delete(queryWrapper);
        if (delete==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return false;
    }

    @Override
    public boolean updateById(LchHtmlInfo entity) {
        return false;
    }

    @Override
    public boolean update(LchHtmlInfo entity, Wrapper<LchHtmlInfo> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<LchHtmlInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(LchHtmlInfo entity) {
        return false;
    }

    @Override
    public LchHtmlInfo getById(Serializable id) {
        return null;
    }

    @Override
    public Collection<LchHtmlInfo> listByIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public Collection<LchHtmlInfo> listByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public LchHtmlInfo getOne(Wrapper<LchHtmlInfo> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<LchHtmlInfo> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<LchHtmlInfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public int count(Wrapper<LchHtmlInfo> queryWrapper) {
        return 0;
    }

    @Override
    public List<LchHtmlInfo> list(Wrapper<LchHtmlInfo> queryWrapper) {

        return mapper.selectList(queryWrapper);
    }

    @Override
    public IPage<LchHtmlInfo> page(IPage<LchHtmlInfo> page, Wrapper<LchHtmlInfo> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<LchHtmlInfo> queryWrapper) {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Wrapper<LchHtmlInfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<LchHtmlInfo> page, Wrapper<LchHtmlInfo> queryWrapper) {
        return null;
    }

    @Override
    public BaseMapper<LchHtmlInfo> getBaseMapper() {
        return null;
    }
}
