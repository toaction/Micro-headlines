package service

import (
	"errors"
	"github.com/action/micro-headlines/dao"
	"github.com/action/micro-headlines/models"
	"github.com/action/micro-headlines/vo"
)

type NewsHeadlineService struct {
	headlineDao *dao.NewsHeadlineDao
}

func NewNewsHeadlineService(headlineDao *dao.NewsHeadlineDao) *NewsHeadlineService {
	return &NewsHeadlineService{headlineDao: headlineDao}
}

// FindPage 分页查询新闻
func (s *NewsHeadlineService) FindPage(queryVo *vo.HeadlineQueryVo) (map[string]interface{}, error) {
	pageData, err := s.headlineDao.FindPageList(queryVo)
	if err != nil {
		return nil, err
	}
	
	totalSize, err := s.headlineDao.FindPageCount(queryVo)
	if err != nil {
		return nil, err
	}
	
	// 计算总页数
	totalPage := int(totalSize) / queryVo.PageSize
	if int(totalSize)%queryVo.PageSize != 0 {
		totalPage++
	}
	
	result := map[string]interface{}{
		"pageNum":   queryVo.PageNum,
		"pageSize":  queryVo.PageSize,
		"totalSize": totalSize,
		"totalPage": totalPage,
		"pageData":  pageData,
	}
	
	return result, nil
}

// FindHeadlineDetail 查询新闻详情（包含浏览量增加）
func (s *NewsHeadlineService) FindHeadlineDetail(hid int) (*vo.HeadlineDetailVo, error) {
	// 增加浏览量
	err := s.headlineDao.IncrPageViews(hid)
	if err != nil {
		return nil, err
	}
	
	return s.headlineDao.FindHeadlineDetail(hid)
}

// Add 添加新闻
func (s *NewsHeadlineService) Add(headline *models.NewsHeadline) error {
	err := s.headlineDao.Add(headline)
	if err != nil {
		return errors.New("添加新闻失败")
	}
	return nil
}

// FindByHid 根据 ID 查询新闻
func (s *NewsHeadlineService) FindByHid(hid int) (*models.NewsHeadline, error) {
	return s.headlineDao.FindByHid(hid)
}

// Update 更新新闻
func (s *NewsHeadlineService) Update(headline *models.NewsHeadline) error {
	err := s.headlineDao.Update(headline)
	if err != nil {
		return errors.New("修改新闻失败")
	}
	return nil
}

// RemoveByHid 删除新闻
func (s *NewsHeadlineService) RemoveByHid(hid int) error {
	err := s.headlineDao.RemoveByHid(hid)
	if err != nil {
		return errors.New("删除新闻失败")
	}
	return nil
}

