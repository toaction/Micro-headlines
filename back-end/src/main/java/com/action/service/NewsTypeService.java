package com.action.service;


import com.action.entity.NewsType;

import java.util.List;

public interface NewsTypeService {
    /**
     * 查询所有头条类型
     */
    List<NewsType> findAll();
}
