package com.action.service.impl;

import com.action.common.Code;
import com.action.dao.NewsUserDao;
import com.action.entity.NewsUser;
import com.action.exception.BusinessException;
import com.action.service.NewsUserService;
import com.action.util.MD5Util;
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
