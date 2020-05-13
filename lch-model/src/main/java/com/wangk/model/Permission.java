package com.wangk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @ClassName :Permission
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 9:50
 * @Version :1.0
 **/
@TableName("Permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 7519773473744122303L;

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("permissionUrl")
    private String permissionUrl;
    @TableField("method")
    private String method;
    @TableField("description")
    private String description;


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

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissionUrl='" + permissionUrl + '\'' +
                ", method='" + method + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
