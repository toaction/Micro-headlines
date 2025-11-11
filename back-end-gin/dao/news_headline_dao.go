package dao

import (
	"fmt"

	"github.com/action/micro-headlines/models"
	"github.com/action/micro-headlines/vo"
	"gorm.io/gorm"
)

type NewsHeadlineDao struct {
	db *gorm.DB
}

func NewNewsHeadlineDao(db *gorm.DB) *NewsHeadlineDao {
	return &NewsHeadlineDao{db: db}
}

// FindPageList 分页查询新闻列表
func (d *NewsHeadlineDao) FindPageList(queryVo *vo.HeadlineQueryVo) ([]vo.HeadlinePageVo, error) {
	var result []vo.HeadlinePageVo

	query := d.db.Table("news_headline").
		Select("hid, title, type, page_views as page_views, " +
			"TIMESTAMPDIFF(HOUR, create_time, NOW()) as past_hours, publisher").
		Where("is_deleted = 0")

	// 添加类型过滤
	if queryVo.Type != 0 {
		query = query.Where("type = ?", queryVo.Type)
	}

	// 添加关键字搜索
	if queryVo.KeyWords != "" {
		query = query.Where("title LIKE ?", fmt.Sprintf("%%%s%%", queryVo.KeyWords))
	}

	// 排序和分页
	offset := (queryVo.PageNum - 1) * queryVo.PageSize
	err := query.Order("past_hours ASC, page_views DESC").
		Limit(queryVo.PageSize).
		Offset(offset).
		Find(&result).Error

	return result, err
}

// FindPageCount 查询符合条件的新闻总数
func (d *NewsHeadlineDao) FindPageCount(queryVo *vo.HeadlineQueryVo) (int64, error) {
	var count int64

	query := d.db.Table("news_headline").Where("is_deleted = 0")

	if queryVo.Type != 0 {
		query = query.Where("type = ?", queryVo.Type)
	}

	if queryVo.KeyWords != "" {
		query = query.Where("title LIKE ?", fmt.Sprintf("%%%s%%", queryVo.KeyWords))
	}

	err := query.Count(&count).Error
	return count, err
}

// IncrPageViews 增加浏览量
func (d *NewsHeadlineDao) IncrPageViews(hid int) error {
	return d.db.Model(&models.NewsHeadline{}).
		Where("hid = ?", hid).
		Update("page_views", gorm.Expr("page_views + ?", 1)).Error
}

// FindHeadlineDetail 查询新闻详情
func (d *NewsHeadlineDao) FindHeadlineDetail(hid int) (*vo.HeadlineDetailVo, error) {
	var detail vo.HeadlineDetailVo

	err := d.db.Table("news_headline h").
		Select("h.hid, h.title, h.article, h.type, t.tname as type_name, "+
			"h.page_views as page_views, "+
			"TIMESTAMPDIFF(HOUR, h.create_time, NOW()) as past_hours, "+
			"h.publisher, u.nick_name as author").
		Joins("LEFT JOIN news_type t ON h.type = t.tid").
		Joins("LEFT JOIN news_user u ON h.publisher = u.uid").
		Where("h.hid = ?", hid).
		First(&detail).Error

	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}

	return &detail, nil
}

// Add 添加新闻
func (d *NewsHeadlineDao) Add(headline *models.NewsHeadline) error {
	return d.db.Create(headline).Error
}

// FindByHid 根据 ID 查询新闻
func (d *NewsHeadlineDao) FindByHid(hid int) (*models.NewsHeadline, error) {
	var headline models.NewsHeadline
	err := d.db.Where("hid = ?", hid).First(&headline).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return &headline, nil
}

// Update 更新新闻
func (d *NewsHeadlineDao) Update(headline *models.NewsHeadline) error {
	return d.db.Model(&models.NewsHeadline{}).
		Where("hid = ?", headline.Hid).
		Updates(map[string]interface{}{
			"title":   headline.Title,
			"article": headline.Article,
			"type":    headline.Type,
		}).Error
}

// RemoveByHid 逻辑删除新闻
func (d *NewsHeadlineDao) RemoveByHid(hid int) error {
	return d.db.Model(&models.NewsHeadline{}).
		Where("hid = ?", hid).
		Update("is_deleted", 1).Error
}
