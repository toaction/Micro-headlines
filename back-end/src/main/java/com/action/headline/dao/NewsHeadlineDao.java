package com.action.headline.dao;

import com.action.headline.entity.NewsHeadline;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlinePageVo;
import com.action.headline.entity.vo.HeadlineQueryVo;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface NewsHeadlineDao {
    /**
     * 分页查询新闻
     */
    @Select("<script>" +
            "select hid, title, type, " +
            "page_views as pageViews, " +
            "TIMESTAMPDIFF(HOUR,create_time,now()) as pastHours, " +
            "publisher " +
            "from news_headline " +
            "where is_deleted = 0 " +
            "<if test='type != 0'> " +
            "and type = #{type} " +
            "</if> " +
            "<if test='keyWords != null and keyWords != \"\"'> " +
            "and title like concat('%', #{keyWords}, '%') " +
            "</if> " +
            "order by pastHours ASC, page_views DESC " +
            "limit ${(pageNum - 1) * pageSize}, #{pageSize} " +
            "</script>")
    List<HeadlinePageVo> findPageList(HeadlineQueryVo headlineQueryVo);

    /**
     * 查询符合条件的新闻总数
     */
    @Select("<script>" +
            "select count(1) from news_headline " +
            "where is_deleted = 0 " +
            "<if test='type != 0'> " +
            "and type = #{type} " +
            "</if> " +
            "<if test='keyWords != null and keyWords != \"\"'> " +
            "and title like concat('%', #{keyWords}, '%') " +
            "</if> " +
            "</script>")
    int findPageCount(HeadlineQueryVo headlineQueryVo);

    @Update("update news_headline " +
            "set page_views = page_views + 1 " +
            "where hid = #{hid}")
    int incrPageViews(int hid);


    @Select("select h.hid hid, " +
            "h.title title, " +
            "h.article article, " +
            "h.type type, " +
            "t.tname typeName, " +
            "h.page_views pageViews, " +
            "TIMESTAMPDIFF(HOUR,h.create_time,now()) pastHours, " +
            "h.publisher publisher, " +
            "u.nick_name author " +
            "from news_headline h " +
            "left join news_type t on h.type = t.tid " +
            "left join news_user u on h.publisher = u.uid " +
            "where h.hid = #{hid}")
    HeadlineDetailVo findHeadlineDetail(int hid);

    @Insert("insert into news_headline " +
            "values (DEFAULT,#{title},#{article},#{type},#{publisher},0,now(),now(),0)")
    int add(NewsHeadline newsHeadline);

    @Select("select * from news_headline where hid = #{hid}")
    NewsHeadline findByHid(Integer hid);

    @Update("update news_headline " +
            "set title = #{title}, article = #{article}, type = #{type}, update_time = now() " +
            "where hid = #{hid}")
    int update(NewsHeadline newsHeadline);

    @Update("update news_headline set is_deleted = 1 where hid = #{hid}")
    int removeByHid(int hid);
}
