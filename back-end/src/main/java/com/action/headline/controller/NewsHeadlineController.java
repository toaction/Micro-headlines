package com.action.headline.controller;

import com.action.headline.common.Code;
import com.action.headline.common.Result;
import com.action.headline.entity.NewsHeadline;
import com.action.headline.entity.vo.HeadlineDetailVo;
import com.action.headline.entity.vo.HeadlineQueryVo;
import com.action.headline.service.NewsHeadlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/headlines")
public class NewsHeadlineController {

    @Autowired
    private NewsHeadlineService headlineService;


    /**
     * 根据头条id查询头条信息
     * 当用户点击修改按钮时，会调用此接口，将信息回显
     */
    @GetMapping("/{hid}")
    protected Result findHeadlineByHid(@PathVariable int hid) {
        NewsHeadline headline = headlineService.findByHid(hid);

        if (headline != null) {
            Map<Object, Object> data = new HashMap<>();
            data.put("headline", headline);
            return Result.ok(data);
        }

        return Result.error(Code.HEADLINE_NOT_EXIST, "该新闻不存在");
    }


    /**
     * 删除头条
     * @param hid
     * @return
     */
    @DeleteMapping("/{hid}")
    public Result delete(@PathVariable int hid) {
        headlineService.removeByHid(hid);
        return Result.ok(null);
    }


    /**
     * 修改头条信息
     * @param newsHeadline
     * @return
     */
    @PutMapping
    public Result update(@RequestBody NewsHeadline newsHeadline) {
        headlineService.update(newsHeadline);
        return Result.ok(null);
    }

    /**
     * 发布头条
     * @param newsHeadline
     * @return
     */
    @PostMapping
    public Result publish(@RequestBody NewsHeadline newsHeadline) {
        headlineService.add(newsHeadline);
        return Result.ok(null);
    }

    /**
     * 查看头条详情
     * 包括发布人昵称
     * @param hid
     * @return
     */
    @GetMapping("/detail/{hid}")
    public Result findDetail(@PathVariable int hid) {
        HeadlineDetailVo detailVo = headlineService.findHeadlineDetail(hid);
        if (detailVo != null) {
            Map<Object, Object> data = new HashMap<>();
            data.put("headline", detailVo);
            return Result.ok(data);
        }
        return Result.error(Code.HEADLINE_NOT_EXIST, "该新闻不存在");
    }

    /**
     * 分页查询
     * @param headlineQueryVo
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestBody HeadlineQueryVo headlineQueryVo) {
        Map<Object, Object> data = headlineService.findPage(headlineQueryVo);
        return Result.ok(data);
    }
}
