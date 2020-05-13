package com.wangk.service.LchServiceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangk.model.Permission;
import com.wangk.service.PermissionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName :PermissionServiceImpl
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 10:08
 * @Version :1.0
 **/
@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public boolean save(Permission entity) {
        return false;
    }

    @Override
    public boolean saveBatch(Collection<Permission> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Permission> entityList, int batchSize) {
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
    public boolean remove(Wrapper<Permission> queryWrapper) {
        return false;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return false;
    }

    @Override
    public boolean updateById(Permission entity) {
        return false;
    }

    @Override
    public boolean update(Permission entity, Wrapper<Permission> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Permission> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Permission entity) {
        return false;
    }

    @Override
    public Permission getById(Serializable id) {
        return null;
    }

    @Override
    public Collection<Permission> listByIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public Collection<Permission> listByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Permission getOne(Wrapper<Permission> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Permission> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Permission> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public int count(Wrapper<Permission> queryWrapper) {
        return 0;
    }

    @Override
    public List<Permission> list(Wrapper<Permission> queryWrapper) {
        return null;
    }

    @Override
    public IPage<Permission> page(IPage<Permission> page, Wrapper<Permission> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<Permission> queryWrapper) {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Wrapper<Permission> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<Permission> page, Wrapper<Permission> queryWrapper) {
        return null;
    }

    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return null;
    }
}
