package vo

// HeadlineQueryVo 查询参数
type HeadlineQueryVo struct {
	KeyWords string `form:"keyWords" json:"keyWords"` // 搜索关键字
	Type     int    `form:"type" json:"type"`         // 新闻类型
	PageNum  int    `form:"pageNum" json:"pageNum"`   // 当前页码
	PageSize int    `form:"pageSize" json:"pageSize"` // 每页记录数
}

// HeadlinePageVo 分页列表项
type HeadlinePageVo struct {
	Hid       int    `json:"hid"`
	Title     string `json:"title"`
	Type      int    `json:"type"`
	PageViews int    `json:"pageViews"`
	PastHours int64  `json:"pastHours"`
	Publisher int    `json:"publisher"`
}

// HeadlineDetailVo 新闻详情
type HeadlineDetailVo struct {
	Hid       int    `json:"hid"`       // 头条id
	Title     string `json:"title"`     // 头条标题
	Article   string `json:"article"`   // 头条内容
	Type      int    `json:"type"`      // 头条类型
	TypeName  string `json:"typeName"`  // 头条类型名称
	PageViews int    `json:"pageViews"` // 头条浏览量
	PastHours int64  `json:"pastHours"` // 头条发布时间(小时)
	Publisher int    `json:"publisher"` // 头条发布者 id
	Author    string `json:"author"`    // 头条作者昵称
}

// HeadlinePublishVo 发布头条请求参数
type HeadlinePublishVo struct {
	Title   string `json:"title" binding:"required"`   // 头条标题（必填）
	Article string `json:"article" binding:"required"` // 头条内容（必填）
	Type    int    `json:"type" binding:"required"`    // 头条类型（必填）
}
