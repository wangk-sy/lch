package com.wangk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName :Role
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 9:52
 * @Version :1.0
 **/
@TableName("Role")
public class Role implements Serializable {
    private static final long serialVersionUID = 6183859181958112451L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("roleLevel")
    private Integer roleLevel;
    @TableField("description")
    private String description;
    @TableField(exist = false)
    private List<Permission> permis;

    public List<Permission> getPermis() {
        return permis;
    }

    public void setPermis(List<Permission> permis) {
        this.permis = permis;
    }

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

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roleLevel=" + roleLevel +
                ", description='" + description + '\'' +
                '}';
    }
}
