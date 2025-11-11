package models

// NewsUser 用户实体类
type NewsUser struct {
	Uid      int    `gorm:"column:uid;primaryKey;autoIncrement" json:"uid"`
	Username string `gorm:"column:username;type:varchar(100);not null" json:"username"`
	UserPwd  string `gorm:"column:user_pwd;type:varchar(100);not null" json:"userPwd,omitempty"`
	NickName string `gorm:"column:nick_name;type:varchar(100)" json:"nickName"`
}

// TableName 指定表名
func (NewsUser) TableName() string {
	return "news_user"
}

