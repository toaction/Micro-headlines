package com.action.service;


import com.action.entity.NewsUser;

public interface NewsUserService {
    /**
     * 根据用户名查询用户信息
     */
    NewsUser findByUsername(String username);

    /**
     * 根据用户id查询用户信息
     */
    NewsUser findByUid(Integer userId);

    /**
     * 注册用户
     */
    boolean register(NewsUser registerUser);
}
