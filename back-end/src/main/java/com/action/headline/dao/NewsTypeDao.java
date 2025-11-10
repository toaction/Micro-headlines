package com.action.headline.dao;

import com.action.headline.entity.NewsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface NewsTypeDao {
    /**
     *
     * @return
     */
    @Select("select tid,tname from news_type")
    List<NewsType> findAll();
}
