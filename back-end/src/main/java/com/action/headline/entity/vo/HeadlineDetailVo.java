package com.action.headline.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadlineDetailVo implements Serializable {
    private Integer hid;            // 头条id
    private String title;           // 头条标题
    private String article;         // 头条内容
    private Integer type;           // 头条类型
    private String typeName;        // 头条类型名称
    private Integer pageViews;      // 头条浏览量
    private Long pastHours;         // 头条发布时间(小时)
    private Integer publisher;      // 头条发布者 id
    private String author;          // 头条作者昵称
}