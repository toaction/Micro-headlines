package com.action.headline.service.impl;

import com.action.headline.dao.NewsTypeDao;
import com.action.headline.dao.impl.NewsTypeDaoImpl;
import com.action.headline.entity.NewsType;
import com.action.headline.service.NewsTypeService;

import java.util.List;

public class NewsTypeServiceImpl implements NewsTypeService {
    private NewsTypeDao typeDao = new NewsTypeDaoImpl();

    @Override
    public List<NewsType> findAll() {
        return typeDao.findAll();
    }
}
