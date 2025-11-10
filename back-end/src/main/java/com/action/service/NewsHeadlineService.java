package com.action.service;

import com.action.entity.NewsHeadline;
import com.action.entity.vo.HeadlineDetailVo;
import com.action.entity.vo.HeadlineQueryVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


public interface NewsHeadlineService {
    /**
     * 分页查询头条信息
     */
    Map<Object, Object> findPage(HeadlineQueryVo headlineQueryVo);

    /**
     * 查询头条详情
     */
    @Transactional
    HeadlineDetailVo findHeadlineDetail(int hid);

    /**
     * 添加头条
     */
    boolean add(NewsHeadline newsHeadline);

    /**
     * 查询头条
     */
    NewsHeadline findByHid(Integer hid);

    /**
     * 修改头条
     */
    boolean update(NewsHeadline newsHeadline);


    /**
     * 删除头条
     */
    boolean removeByHid(int hid);
}
