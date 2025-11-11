package dao

import (
	"github.com/action/micro-headlines/models"
	"gorm.io/gorm"
)

type NewsUserDao struct {
	db *gorm.DB
}

func NewNewsUserDao(db *gorm.DB) *NewsUserDao {
	return &NewsUserDao{db: db}
}

// FindByUsername 根据用户名查询用户
func (d *NewsUserDao) FindByUsername(username string) (*models.NewsUser, error) {
	var user models.NewsUser
	err := d.db.Where("username = ?", username).First(&user).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return &user, nil
}

// FindByUid 根据用户 ID 查询用户
func (d *NewsUserDao) FindByUid(uid int) (*models.NewsUser, error) {
	var user models.NewsUser
	err := d.db.Where("uid = ?", uid).First(&user).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return &user, nil
}

// Add 添加用户
func (d *NewsUserDao) Add(user *models.NewsUser) error {
	return d.db.Create(user).Error
}

