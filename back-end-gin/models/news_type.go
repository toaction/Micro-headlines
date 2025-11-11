package models

// NewsType 新闻类型实体类
type NewsType struct {
	Tid   int    `gorm:"column:tid;primaryKey;autoIncrement" json:"tid"`
	Tname string `gorm:"column:tname;type:varchar(100);not null" json:"tname"`
}

// TableName 指定表名
func (NewsType) TableName() string {
	return "news_type"
}

