package com.action.headline.service.impl;

import com.action.headline.common.Code;
import com.action.headline.dao.NewsHeadlineDao;
import com.action.headline.entity.NewsHeadline;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlinePageVo;
import com.action.headline.entity.vo.HeadlineQueryVo;
import com.action.headline.exception.SystemException;
import com.action.headline.service.NewsHeadlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsHeadlineServiceImpl implements NewsHeadlineService {

    @Autowired
    private NewsHeadlineDao headlineDao;


    /**
     * 分页查询
     */
    @Override
    public Map<Object, Object> findPage(HeadlineQueryVo headlineQueryVo) {
        int pageNum = headlineQueryVo.getPageNum();
        int pageSize = headlineQueryVo.getPageSize();
        List<HeadlinePageVo> pageData = headlineDao.findPageList(headlineQueryVo);
        int totalSize = headlineDao.findPageCount(headlineQueryVo);

        int totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        Map<Object, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageNum", pageNum);
        pageInfo.put("pageSize", pageSize);
        pageInfo.put("totalSize", totalSize);
        pageInfo.put("totalPage", totalPage);
        pageInfo.put("pageData", pageData);
        return pageInfo;
    }

    @Override
    public HeadlineDetailVo findHeadlineDetail(int hid) {
        // 浏览量加 1
        headlineDao.incrPageViews(hid);
        return headlineDao.findHeadlineDetail(hid);
    }

    @Override
    public boolean add(NewsHeadline newsHeadline) {
        try {
            headlineDao.add(newsHeadline);
        }catch (Exception e) {
            throw new SystemException("添加新闻失败", Code.HEADLINE_ADD_ERROR);
        }
        return true;
    }

    @Override
    public NewsHeadline findByHid(Integer hid) {
        return headlineDao.findByHid(hid);
    }

    @Override
    public boolean update(NewsHeadline newsHeadline) {
        try {
            headlineDao.update(newsHeadline);
        }catch (Exception e) {
            throw new SystemException("修改新闻失败", Code.HEADLINE_UPDATE_ERROR);
        }
        return true;
    }

    @Override
    public boolean removeByHid(int hid) {
        try {
            headlineDao.removeByHid(hid);
        }catch (Exception e) {
            throw new SystemException("删除新闻失败", Code.HEADLINE_DELETE_ERROR);
        }
        return true;
    }
}
