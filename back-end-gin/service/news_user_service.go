package service

import (
	"errors"
	"github.com/action/micro-headlines/dao"
	"github.com/action/micro-headlines/models"
	"github.com/action/micro-headlines/utils"
)

type NewsUserService struct {
	userDao *dao.NewsUserDao
}

func NewNewsUserService(userDao *dao.NewsUserDao) *NewsUserService {
	return &NewsUserService{userDao: userDao}
}

// FindByUsername 根据用户名查询用户
func (s *NewsUserService) FindByUsername(username string) (*models.NewsUser, error) {
	return s.userDao.FindByUsername(username)
}

// FindByUid 根据用户 ID 查询用户
func (s *NewsUserService) FindByUid(uid int) (*models.NewsUser, error) {
	return s.userDao.FindByUid(uid)
}

// Register 注册用户
func (s *NewsUserService) Register(user *models.NewsUser) error {
	// 密码加密
	user.UserPwd = utils.Encrypt(user.UserPwd)
	
	err := s.userDao.Add(user)
	if err != nil {
		return errors.New("用户注册失败")
	}
	
	return nil
}

