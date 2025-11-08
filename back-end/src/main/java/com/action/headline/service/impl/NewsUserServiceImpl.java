package com.action.headline.service.impl;

import com.action.headline.dao.NewsUserDao;
import com.action.headline.dao.impl.NewsUserDaoImpl;
import com.action.headline.entity.NewsUser;
import com.action.headline.service.NewsUserService;
import com.action.headline.util.MD5Util;

public class NewsUserServiceImpl implements NewsUserService {

    private NewsUserDao userDao = new NewsUserDaoImpl();

    @Override
    public NewsUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public NewsUser findByUid(Integer userId) {
        return userDao.findByUid(userId);
    }

    @Override
    public Integer registerUser(NewsUser registerUser) {
        registerUser.setUserPwd(MD5Util.encrypt(registerUser.getUserPwd()));
        return userDao.insertUser(registerUser);
    }
}
