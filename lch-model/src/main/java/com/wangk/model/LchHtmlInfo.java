package com.wangk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @ClassName :LchHtmlInfo
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 9:51
 * @Version :1.0
 **/
@TableName("LchHtmlInfo")
@Document(indexName = "wangk",type = "LchHtmlInfo")
public class LchHtmlInfo implements Serializable {

    private static final long serialVersionUID = 8395053010157283754L;
    @TableId(value = "id",type = IdType.AUTO)
    @Id
    private Integer id;

    //type:字段类型 有text表示存储数据时候会自动分词，并生成索引；keyword表示存储数据时候不会分词建立索引
    //analyzer：分词器名称
    //index：是否索引 boolean类型 默认true
    //store：是否存储 boolean类型 默认false
    @TableField("name")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @TableField("url")
    @Field(type = FieldType.Keyword)
    private String url;

    @TableField("typeId")
    @Field
    private Integer typeId;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
