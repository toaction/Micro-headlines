package com.action.headline.service;

import com.action.headline.config.SpringConfig;
import com.action.headline.entity.NewsUser;
import com.action.headline.util.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class NewsUserServiceTest {

    @Autowired
    private NewsUserService userService;

    @Test
    public void testFindByUsername() {
        NewsUser user = userService.findByUsername("zhangsan");
        System.out.println(user);
    }

    @Test
    public void testFindByUid() {
        NewsUser user = userService.findByUid(1);
        System.out.println(user);
    }

//    @Test
//    public void testRegister() {
//        NewsUser registerUser = new NewsUser();
//        registerUser.setUsername("wangwu");
//        registerUser.setUserPwd(MD5Util.encrypt("123456"));
//        registerUser.setNickName("王五");
//        boolean result = userService.register(registerUser);
//        System.out.println(result);
//    }
}
