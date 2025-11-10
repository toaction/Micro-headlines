package com.action.controller;

import com.action.common.Code;
import com.action.common.Result;
import com.action.entity.NewsUser;
import com.action.exception.BusinessException;
import com.action.service.NewsUserService;
import com.action.util.JwtUtil;
import com.action.util.MD5Util;
import com.action.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class NewsUserController {


    @Autowired
    private NewsUserService userService;

    /**
     * 检查用户名是否已存在
     * @param username
     * @return
     */
    @GetMapping("/check/{username}")
    public Result checkUserName(@PathVariable String username) {
        NewsUser newsUser = userService.findByUsername(username);
        if (newsUser != null) {
            return Result.error(Code.USERNAME_USED, "用户名已存在");
        }
        return Result.ok(null);
    }

    /**
     * 根据 uid 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public Result getUserInfo() {
        int userId = ThreadLocalUtil.getUserId();
        NewsUser user = userService.findByUid(userId);
        if (user != null) {
            Map<Object, Object> data = new HashMap<>();
            user.setUserPwd(null);
            data.put("user", user);
            return Result.ok(data);
        }
        return Result.error(Code.USER_NOT_EXIST, "用户不存在");
    }

    /**
     * 用户登录
     * @param loginUser
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody NewsUser loginUser) {
        if (loginUser == null) {
            throw new BusinessException("用户登录参数不能为空", Code.REQUEST_ERROR);
        }

        NewsUser user = userService.findByUsername(loginUser.getUsername());
        Result result = Result.error(Code.USERNAME_OR_PASSWORD_ERROR, "用户名或密码错误");
        // 验证用户名密码
        if (user != null && MD5Util.encrypt(loginUser.getUserPwd()).equalsIgnoreCase(user.getUserPwd())) {
            Map<Object, Object> data = new HashMap<>();
            data.put("token", JwtUtil.createToken(user.getUid()));
            result = Result.ok(data);
        }

        return result;
    }


    /**
     * 用户注册
     * @param registerUser
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody NewsUser registerUser) {
        if (registerUser == null) {
            throw new BusinessException("用户注册参数不能为空", Code.REQUEST_ERROR);
        }

        userService.register(registerUser);
        return Result.ok(null);
    }
}
