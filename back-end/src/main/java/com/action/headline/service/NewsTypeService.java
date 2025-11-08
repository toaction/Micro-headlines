package com.action.headline.service;

import com.action.headline.entity.NewsType;

import java.util.List;

public interface NewsTypeService {
    /**
     * 查询所有头条类型
     */
    List<NewsType> findAll();
}
