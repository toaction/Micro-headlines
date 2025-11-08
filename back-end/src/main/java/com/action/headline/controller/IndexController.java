package com.action.headline.controller;

import com.action.headline.common.Result;
import com.action.headline.common.ResultCodeEnum;
import com.action.headline.entity.NewsType;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlineQueryVo;
import com.action.headline.service.NewsHeadlineService;
import com.action.headline.service.NewsTypeService;
import com.action.headline.service.impl.NewsHeadlineServiceImpl;
import com.action.headline.service.impl.NewsTypeServiceImpl;
import com.action.headline.util.WebUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 新闻头条主页
* 无需登录
* 查看新闻列表、查看新闻详情
* */
@WebServlet("/index/*")
public class IndexController extends BaseController {

    private final NewsHeadlineService headlineService = new NewsHeadlineServiceImpl();

    private final NewsTypeService typeService = new NewsTypeServiceImpl();


    /**
     * 获取所有新闻类型
     */
    protected void findAllNewsType(HttpServletRequest req, HttpServletResponse resp) {
        List<NewsType> newsTypeList = typeService.findAll();

        Result<Object> result = Result.ok(newsTypeList);
        if (newsTypeList == null) {
            result = Result.build(null, ResultCodeEnum.NOT_FOUND);
        }
        WebUtil.writeJson(resp, result);
    }

    /**
     * 展示头条详情
     * 前端请求参数携带 hid
     */
    protected void showHeadlineDetail(HttpServletRequest req, HttpServletResponse resp) {
        String hidParam = req.getParameter("hid");

        int hid;
        try {
            hid = Integer.parseInt(hidParam);
        } catch (NumberFormatException e) {
            WebUtil.writeJson(resp, Result.build(null, ResultCodeEnum.PARAM_ERROR));
            return;
        }

        HeadlineDetailVo headlineDetailVo = headlineService.findHeadlineDetail(hid);

        if (headlineDetailVo == null) {
            WebUtil.writeJson(resp, Result.build(null, ResultCodeEnum.NOT_FOUND));
            return;
        }

        final Map<Object, Object> data = new HashMap<>();
        data.put("headline", headlineDetailVo);
        WebUtil.writeJson(resp, Result.ok(data));
    }

    /**
     * 分页查询头条信息
     * 前端请求体携带 keyWords、type、pageNum、pageSize
     */
    protected void findNewsPage(HttpServletRequest req, HttpServletResponse resp) {
        HeadlineQueryVo headlineQueryVo = WebUtil.readJson(req, HeadlineQueryVo.class);
        if (headlineQueryVo == null) {
            WebUtil.writeJson(resp, Result.build(null, ResultCodeEnum.PARAM_ERROR));
            return;
        }

        Map<Object, Object> pageInfo = headlineService.findPage(headlineQueryVo);
        Map<Object, Object> data = new HashMap<>();
        data.put("pageInfo", pageInfo);
        WebUtil.writeJson(resp, Result.ok(data));
    }
}
