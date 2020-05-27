package com.wangk.mapper;
import	java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangk.model.HtmlType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HtmlTypeMapper extends BaseMapper<HtmlType> {

    List<HtmlType> getTypeByWhere();

}
