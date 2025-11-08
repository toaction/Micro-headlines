package com.action.headline.dao;

import com.action.headline.entity.NewsType;

import java.util.List;

public interface NewsTypeDao {
    /**
     *
     * @return
     */
    List<NewsType> findAll();
}
