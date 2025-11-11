package controller

import (
	"github.com/action/micro-headlines/common"
	"github.com/action/micro-headlines/service"
	"github.com/gin-gonic/gin"
)

type NewsTypeController struct {
	typeService *service.NewsTypeService
}

func NewNewsTypeController(typeService *service.NewsTypeService) *NewsTypeController {
	return &NewsTypeController{typeService: typeService}
}

// FindAll 获取所有新闻类型
func (ctrl *NewsTypeController) FindAll(c *gin.Context) {
	types, err := ctrl.typeService.FindAll()
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}
	
	common.Ok(c, types)
}

