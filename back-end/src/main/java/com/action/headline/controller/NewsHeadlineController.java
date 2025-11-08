package com.action.headline.controller;

import com.action.headline.common.Result;
import com.action.headline.common.ResultCodeEnum;
import com.action.headline.entity.NewsHeadline;
import com.action.headline.service.NewsHeadlineService;
import com.action.headline.service.impl.NewsHeadlineServiceImpl;
import com.action.headline.util.JwtUtil;
import com.action.headline.util.WebUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/headline/*")
public class NewsHeadlineController extends BaseController {
    private NewsHeadlineService headlineService = new NewsHeadlineServiceImpl();


    /**
     * 删除头条
     */
    protected void removeByHid(HttpServletRequest req, HttpServletResponse resp) {
        int hid = Integer.parseInt(req.getParameter("hid"));
        headlineService.removeByHid(hid);
        WebUtil.writeJson(resp, Result.ok(null));
    }

    /**
     * 更新头条
     */

    protected void update(HttpServletRequest req, HttpServletResponse resp) {
        NewsHeadline newsHeadline = WebUtil.readJson(req, NewsHeadline.class);
        headlineService.update(newsHeadline);
        WebUtil.writeJson(resp, Result.ok(null));
    }

    /**
     * 根据头条id查询头条信息
     * 当用户点击修改按钮时，会调用此接口，将信息回显
     */
    protected void findHeadlineByHid(HttpServletRequest req, HttpServletResponse resp) {
        Integer hid = Integer.parseInt(req.getParameter("hid"));
        NewsHeadline headline = headlineService.findByHid(hid);

        Result<Object> result = Result.build(null, ResultCodeEnum.NOT_FOUND);
        if (headline != null) {
            Map<Object, Object> data = new HashMap<>();
            data.put("headline", headline);
            result = Result.ok(data);
        }

        WebUtil.writeJson(resp, result);
    }

    /**
     * 发布头条
     */
    protected void publish(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader("token");
        Integer userId = JwtUtil.getUserId(token);
        NewsHeadline newsHeadline = WebUtil.readJson(req, NewsHeadline.class);
        newsHeadline.setPublisher(userId);
        headlineService.addNewsHeadline(newsHeadline);
        WebUtil.writeJson(resp, Result.ok(null));
    }
}
