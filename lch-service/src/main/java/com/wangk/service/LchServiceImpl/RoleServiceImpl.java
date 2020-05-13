package com.wangk.service.LchServiceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangk.model.Role;
import com.wangk.service.RoleService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName :RoleServiceImpl
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 10:08
 * @Version :1.0
 **/
@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public boolean save(Role entity) {
        return false;
    }

    @Override
    public boolean saveBatch(Collection<Role> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Role> entityList, int batchSize) {
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
    public boolean remove(Wrapper<Role> queryWrapper) {
        return false;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return false;
    }

    @Override
    public boolean updateById(Role entity) {
        return false;
    }

    @Override
    public boolean update(Role entity, Wrapper<Role> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Role> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Role entity) {
        return false;
    }

    @Override
    public Role getById(Serializable id) {
        return null;
    }

    @Override
    public Collection<Role> listByIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public Collection<Role> listByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Role getOne(Wrapper<Role> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Role> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public int count(Wrapper<Role> queryWrapper) {
        return 0;
    }

    @Override
    public List<Role> list(Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public IPage<Role> page(IPage<Role> page, Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Wrapper<Role> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<Role> page, Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return null;
    }
}
