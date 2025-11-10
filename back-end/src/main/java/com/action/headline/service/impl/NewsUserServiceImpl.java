package com.action.headline.service.impl;

import com.action.headline.common.Code;
import com.action.headline.dao.NewsUserDao;
import com.action.headline.entity.NewsUser;
import com.action.headline.exception.BusinessException;
import com.action.headline.service.NewsUserService;
import com.action.headline.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsUserServiceImpl implements NewsUserService {

    @Autowired
    private NewsUserDao userDao;

    @Override
    public NewsUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public NewsUser findByUid(Integer userId) {
        return userDao.findByUid(userId);
    }

    @Override
    public boolean register(NewsUser registerUser) {
        // 密码加密
        registerUser.setUserPwd(MD5Util.encrypt(registerUser.getUserPwd()));
        try {
            userDao.add(registerUser);
        }catch (Exception e) {
            throw new BusinessException("用户注册失败", Code.REGISTER_ERROR);
        }
        return true;
    }
}
