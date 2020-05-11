package com.wangk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangk.model.LchHtmlInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName :LchMapper
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 10:46
 * @Version :1.0
 **/
@Mapper
public interface LchMapper extends BaseMapper<LchHtmlInfo> {

/*    List<LchHtmlInfo> getAllUrl();

    Integer insert(LchHtmlInfo lchHtmlInfo);

    List<LchHtmlInfo> getUrlByPage(Integer page, Integer size);


    int delete(Integer id);*/
}
