package com.action.headline.controller;

import com.action.headline.common.Result;
import com.action.headline.common.ResultCodeEnum;
import com.action.headline.entity.NewsUser;
import com.action.headline.service.NewsUserService;
import com.action.headline.service.impl.NewsUserServiceImpl;
import com.action.headline.util.JwtUtil;
import com.action.headline.util.MD5Util;
import com.action.headline.util.WebUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/*")
public class NewsUserController extends BaseController {


    private NewsUserService userService = new NewsUserServiceImpl();


    /**
     * 校验用户名是否已存在
     */
    protected void checkUserName(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        NewsUser newsUser = userService.findByUsername(username);

        Result<Object> result = Result.ok(null);
        if (null != newsUser) {
            result = Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        WebUtil.writeJson(resp, result);
    }

    /**
     * 用户登录
     * 前端请求体携带 username、userPwd
     * 返回结果：JWT 令牌
     */
    protected void login(HttpServletRequest req, HttpServletResponse resp) {
        NewsUser loginUser = WebUtil.readJson(req, NewsUser.class);
        if (loginUser == null) {
            WebUtil.writeJson(resp, Result.build(null, ResultCodeEnum.PARAM_ERROR));
            return;
        }

        NewsUser user = userService.findByUsername(loginUser.getUsername());
        Result<Object> result = Result.build(null, ResultCodeEnum.USERNAME_OR_PASSWORD_ERROR);

        if (user != null && MD5Util.encrypt(loginUser.getUserPwd()).equalsIgnoreCase(user.getUserPwd())) {
            Map<Object, Object> data = new HashMap<>();
            data.put("token", JwtUtil.createToken(user.getUid()));
            result = Result.ok(data);
        }

        WebUtil.writeJson(resp, result);
    }


    /**
     * 用户注册
     */
    protected void register(HttpServletRequest req, HttpServletResponse resp) {
        NewsUser registerUser = WebUtil.readJson(req, NewsUser.class);

        Integer rows = userService.registerUser(registerUser);
        Result<Object> result = Result.ok(null);

        if (rows == 0) {
            result = Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        WebUtil.writeJson(resp, result);
    }

    /**
     * 前端校验是否已登录
     */
    protected void checkLogin(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader("token");

        Result<Object> result = Result.build(null, ResultCodeEnum.NOT_LOGIN);
        if (token != null && JwtUtil.validateToken(token)) {
            result = Result.ok(null);
        }

        WebUtil.writeJson(resp, result);
    }


    /**
     * 获取用户信息
     */
    protected void getUserInfo(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader("token");
        Result<Object> result = Result.build(null, ResultCodeEnum.NOT_LOGIN);

        if (token != null && JwtUtil.validateToken(token)) {
            Integer userId = JwtUtil.getUserId(token);
            NewsUser newsUser = userService.findByUid(userId);
            if (null != newsUser) {
                Map<Object, Object> data = new HashMap<>();
                newsUser.setUserPwd(null);
                data.put("loginUser", newsUser);
                result = Result.ok(data);
            }
        }

        WebUtil.writeJson(resp, result);
    }
}
