package com.action.headline.service.impl;

import com.action.headline.dao.NewsTypeDao;
import com.action.headline.entity.NewsType;
import com.action.headline.service.NewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsTypeServiceImpl implements NewsTypeService {

    @Autowired
    private NewsTypeDao typeDao;

    @Override
    public List<NewsType> findAll() {
        return typeDao.findAll();
    }
}
