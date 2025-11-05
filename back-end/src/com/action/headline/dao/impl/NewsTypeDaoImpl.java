package com.action.headline.dao.impl;

import com.action.headline.dao.BaseDao;
import com.action.headline.dao.NewsTypeDao;
import com.action.headline.entity.NewsType;

import java.util.List;

public class NewsTypeDaoImpl extends BaseDao implements NewsTypeDao {
    @Override
    public List<NewsType> findAll() {
        String sql = "select tid,tname from news_type";
        return baseQuery(NewsType.class,sql);
    }
}
