package com.action.headline.service;

import com.action.headline.entity.NewsHeadline;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlineQueryVo;

import java.util.Map;

public interface NewsHeadlineService {
    /**
     * 分页查询头条信息
     */
    Map<Object, Object> findPage(HeadlineQueryVo headlineQueryVo);

    /**
     * 查询头条详情
     */
    HeadlineDetailVo findHeadlineDetail(int hid);

    /**
     * 添加头条
     */
    int addNewsHeadline(NewsHeadline newsHeadline);

    /**
     * 查询头条
     */
    NewsHeadline findByHid(Integer hid);

    /**
     * 修改头条
     */
    int update(NewsHeadline newsHeadline);


    /**
     * 删除头条
     */
    int removeByHid(int hid);
}
