package com.action.headline.controller;


import com.action.headline.common.Result;
import com.action.headline.entity.NewsType;
import com.action.headline.service.NewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/types")
public class NewsTypeController {

    @Autowired
    private NewsTypeService typeService;

    /**
     * 获取所有新闻类型
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<NewsType> newsTypeList = typeService.findAll();
        return Result.ok(newsTypeList);
    }
}
