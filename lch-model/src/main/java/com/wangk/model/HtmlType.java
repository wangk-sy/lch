package com.wangk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("HtmlType")
public class HtmlType {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField(value ="name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HtmlType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
