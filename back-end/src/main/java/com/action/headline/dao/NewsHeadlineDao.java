package com.action.headline.dao;

import com.action.headline.entity.NewsHeadline;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlinePageVo;
import com.action.headline.entity.vo.HeadlineQueryVo;

import java.util.List;

public interface NewsHeadlineDao {
    /**
     * 分页查询新闻
     */
    List<HeadlinePageVo> findPageList(HeadlineQueryVo headlineQueryVo);

    /**
     * 查询符合条件的新闻总数
     */
    int findPageCount(HeadlineQueryVo headlineQueryVo);

    int incrPageViews(int hid);

    HeadlineDetailVo findHeadlineDetail(int hid);

    int addNewsHeadline(NewsHeadline newsHeadline);

    NewsHeadline findByHid(Integer hid);

    int update(NewsHeadline newsHeadline);

    int removeByHid(int hid);
}
