package com.action.controller;

import com.action.common.Result;
import com.action.entity.NewsType;
import com.action.service.NewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 新闻类型控制器
 */
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
