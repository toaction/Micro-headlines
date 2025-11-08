package com.action.headline.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadlineQueryVo implements Serializable {
    private String keyWords;            // 搜索关键字
    private Integer type ;              // 新闻类型
    private Integer pageNum;            // 当前页码
    private Integer pageSize;           // 每页记录数
}
