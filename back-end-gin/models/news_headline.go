package models

import "time"

// NewsHeadline 新闻头条实体类
type NewsHeadline struct {
	Hid        int       `gorm:"column:hid;primaryKey;autoIncrement" json:"hid"`
	Title      string    `gorm:"column:title;type:varchar(200);not null" json:"title"`
	Article    string    `gorm:"column:article;type:text" json:"article"`
	Type       int       `gorm:"column:type;not null" json:"type"`
	Publisher  int       `gorm:"column:publisher;not null" json:"publisher"`
	PageViews  int       `gorm:"column:page_views;default:0" json:"pageViews"`
	CreateTime time.Time `gorm:"column:create_time;autoCreateTime" json:"createTime"`
	UpdateTime time.Time `gorm:"column:update_time;autoUpdateTime" json:"updateTime"`
	IsDeleted  int       `gorm:"column:is_deleted;default:0" json:"isDeleted"`
}

// TableName 指定表名
func (NewsHeadline) TableName() string {
	return "news_headline"
}

