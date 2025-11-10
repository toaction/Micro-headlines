package com.action.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 新闻头条实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsHeadline implements Serializable {
    private Integer hid;
    
    private String title;
    
    private String article;
    
    private Integer type;
    
    private Integer publisher;
    
    private Integer pageViews;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer isDeleted;

}