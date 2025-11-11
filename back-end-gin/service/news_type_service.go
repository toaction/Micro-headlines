package service

import (
	"github.com/action/micro-headlines/dao"
	"github.com/action/micro-headlines/models"
)

type NewsTypeService struct {
	typeDao *dao.NewsTypeDao
}

func NewNewsTypeService(typeDao *dao.NewsTypeDao) *NewsTypeService {
	return &NewsTypeService{typeDao: typeDao}
}

// FindAll 查询所有新闻类型
func (s *NewsTypeService) FindAll() ([]models.NewsType, error) {
	return s.typeDao.FindAll()
}

