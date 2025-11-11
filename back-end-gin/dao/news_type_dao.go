package dao

import (
	"github.com/action/micro-headlines/models"
	"gorm.io/gorm"
)

type NewsTypeDao struct {
	db *gorm.DB
}

func NewNewsTypeDao(db *gorm.DB) *NewsTypeDao {
	return &NewsTypeDao{db: db}
}

// FindAll 查询所有新闻类型
func (d *NewsTypeDao) FindAll() ([]models.NewsType, error) {
	var types []models.NewsType
	err := d.db.Find(&types).Error
	return types, err
}

