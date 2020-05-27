package com.wangk.service.LchServiceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangk.mapper.HtmlTypeMapper;
import com.wangk.model.HtmlType;
import com.wangk.service.HtmlTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class HtmlTypeServiceImpl implements HtmlTypeService {

    @Autowired
    private HtmlTypeMapper htmlTypeMapper;

    @Override
    public boolean save(HtmlType entity) {
        return false;
    }

    @Override
    public boolean saveBatch(Collection<HtmlType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<HtmlType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean removeById(Serializable id) {
        return false;
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return false;
    }

    @Override
    public boolean remove(Wrapper<HtmlType> queryWrapper) {
        return false;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return false;
    }

    @Override
    public boolean updateById(HtmlType entity) {
        return false;
    }

    @Override
    public boolean update(HtmlType entity, Wrapper<HtmlType> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<HtmlType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(HtmlType entity) {
        return false;
    }

    @Override
    public HtmlType getById(Serializable id) {
        return null;
    }

    @Override
    public Collection<HtmlType> listByIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public Collection<HtmlType> listByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public HtmlType getOne(Wrapper<HtmlType> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<HtmlType> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<HtmlType> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public int count(Wrapper<HtmlType> queryWrapper) {
        return 0;
    }

    @Override
    public List<HtmlType> list(Wrapper<HtmlType> queryWrapper) {
        return htmlTypeMapper.getTypeByWhere();
    }

    @Override
    public IPage<HtmlType> page(IPage<HtmlType> page, Wrapper<HtmlType> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<HtmlType> queryWrapper) {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Wrapper<HtmlType> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<HtmlType> page, Wrapper<HtmlType> queryWrapper) {
        return null;
    }

    @Override
    public BaseMapper<HtmlType> getBaseMapper() {
        return null;
    }
}
