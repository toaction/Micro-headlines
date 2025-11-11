package controller

import (
	"github.com/action/micro-headlines/common"
	"github.com/action/micro-headlines/models"
	"github.com/action/micro-headlines/service"
	"github.com/action/micro-headlines/utils"
	"github.com/action/micro-headlines/vo"
	"github.com/gin-gonic/gin"
)

type NewsUserController struct {
	userService *service.NewsUserService
}

func NewNewsUserController(userService *service.NewsUserService) *NewsUserController {
	return &NewsUserController{userService: userService}
}

// CheckUserName 检查用户名是否已存在
func (ctrl *NewsUserController) CheckUserName(c *gin.Context) {
	username := c.Param("username")

	user, err := ctrl.userService.FindByUsername(username)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	if user != nil {
		common.Error(c, common.USERNAME_USED, "用户名已存在")
		return
	}

	common.Ok(c, nil)
}

// GetUserInfo 获取用户信息
func (ctrl *NewsUserController) GetUserInfo(c *gin.Context) {
	userId, exists := utils.GetUserId(c)
	if !exists {
		common.Unauthorized(c)
		return
	}

	user, err := ctrl.userService.FindByUid(userId)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	if user == nil {
		common.Error(c, common.USER_NOT_EXIST, "用户不存在")
		return
	}

	// 隐藏密码
	user.UserPwd = ""

	data := map[string]interface{}{
		"user": user,
	}

	common.Ok(c, data)
}

// Login 用户登录
func (ctrl *NewsUserController) Login(c *gin.Context) {
	var req vo.UserRequestVo

	if err := c.ShouldBindJSON(&req); err != nil {
		common.Error(c, common.REQUEST_ERROR, "用户登录参数不能为空: "+err.Error())
		return
	}

	user, err := ctrl.userService.FindByUsername(req.Username)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	// 验证用户名密码
	if user == nil || utils.Encrypt(req.UserPwd) != user.UserPwd {
		common.Error(c, common.USERNAME_OR_PASSWORD_ERROR, "用户名或密码错误")
		return
	}

	// 生成 token
	token, err := utils.CreateToken(user.Uid)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, "生成 token 失败")
		return
	}

	data := map[string]interface{}{
		"token": token,
	}

	common.Ok(c, data)
}

// Register 用户注册
func (ctrl *NewsUserController) Register(c *gin.Context) {
	var req vo.UserRequestVo

	if err := c.ShouldBindJSON(&req); err != nil {
		common.Error(c, common.REQUEST_ERROR, "用户注册参数不能为空: "+err.Error())
		return
	}

	// 转换为数据库模型
	registerUser := &models.NewsUser{
		Username: req.Username,
		UserPwd:  req.UserPwd,
		NickName: req.NickName,
	}

	err := ctrl.userService.Register(registerUser)
	if err != nil {
		common.Error(c, common.REGISTER_ERROR, err.Error())
		return
	}

	common.Ok(c, nil)
}
