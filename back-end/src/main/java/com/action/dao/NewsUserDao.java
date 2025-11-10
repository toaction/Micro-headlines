package com.action.dao;

import com.action.entity.NewsUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NewsUserDao {

    @Select("select uid, username, user_pwd as userPwd, nick_name as nickName " +
            "from news_user " +
            "where username = #{username}")
    NewsUser findByUsername(String username);

    @Select("select uid, username, user_pwd as userPwd, nick_name as nickName " +
            "from news_user " +
            "where uid = #{userId}")
    NewsUser findByUid(Integer userId);


    @Insert("insert into news_user " +
            "values (DEFAULT, #{username}, #{userPwd}, #{nickName})")
    Integer add(NewsUser user);
}
