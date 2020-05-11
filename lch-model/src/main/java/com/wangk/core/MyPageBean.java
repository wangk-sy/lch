package com.wangk.core;

import java.util.List;

/**
 * @ClassName :PageInfo
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/8 12:49
 * @Version :1.0
 **/
public class MyPageBean {
    private Long total;
    private List<?> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
