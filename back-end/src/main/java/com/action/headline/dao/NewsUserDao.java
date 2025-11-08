package com.action.headline.dao;

import com.action.headline.entity.NewsUser;

public interface NewsUserDao {

    NewsUser findByUsername(String username);

    NewsUser findByUid(Integer userId);


    Integer insertUser(NewsUser user);
}
