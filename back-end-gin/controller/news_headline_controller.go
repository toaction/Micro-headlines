package controller

import (
	"strconv"

	"github.com/action/micro-headlines/common"
	"github.com/action/micro-headlines/models"
	"github.com/action/micro-headlines/service"
	"github.com/action/micro-headlines/utils"
	"github.com/action/micro-headlines/vo"
	"github.com/gin-gonic/gin"
)

type NewsHeadlineController struct {
	headlineService *service.NewsHeadlineService
}

func NewNewsHeadlineController(headlineService *service.NewsHeadlineService) *NewsHeadlineController {
	return &NewsHeadlineController{headlineService: headlineService}
}

// FindHeadlineByHid 根据头条 ID 查询头条信息（用于修改时回显）
func (ctrl *NewsHeadlineController) FindHeadlineByHid(c *gin.Context) {
	hidStr := c.Param("hid")
	hid, err := strconv.Atoi(hidStr)
	if err != nil {
		common.Error(c, common.REQUEST_ERROR, "参数错误")
		return
	}

	headline, err := ctrl.headlineService.FindByHid(hid)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	if headline == nil {
		common.Error(c, common.HEADLINE_NOT_EXIST, "该新闻不存在")
		return
	}

	data := map[string]interface{}{
		"headline": headline,
	}

	common.Ok(c, data)
}

// Delete 删除头条
func (ctrl *NewsHeadlineController) Delete(c *gin.Context) {
	hidStr := c.Param("hid")
	hid, err := strconv.Atoi(hidStr)
	if err != nil {
		common.Error(c, common.REQUEST_ERROR, "参数错误")
		return
	}

	err = ctrl.headlineService.RemoveByHid(hid)
	if err != nil {
		common.Error(c, common.HEADLINE_DELETE_ERROR, err.Error())
		return
	}

	common.Ok(c, nil)
}

// Update 修改头条信息
func (ctrl *NewsHeadlineController) Update(c *gin.Context) {
	var headline models.NewsHeadline

	if err := c.ShouldBindJSON(&headline); err != nil {
		common.Error(c, common.REQUEST_ERROR, "参数错误")
		return
	}

	err := ctrl.headlineService.Update(&headline)
	if err != nil {
		common.Error(c, common.HEADLINE_UPDATE_ERROR, err.Error())
		return
	}

	common.Ok(c, nil)
}

// Publish 发布头条
func (ctrl *NewsHeadlineController) Publish(c *gin.Context) {
	var req vo.HeadlinePublishVo

	if err := c.ShouldBindJSON(&req); err != nil {
		common.Error(c, common.REQUEST_ERROR, "参数错误: "+err.Error())
		return
	}

	// 从上下文获取当前用户 ID
	userId, exists := utils.GetUserId(c)
	if !exists {
		common.Unauthorized(c)
		return
	}

	// 转换为数据库模型
	headline := &models.NewsHeadline{
		Title:     req.Title,
		Article:   req.Article,
		Type:      req.Type,
		Publisher: userId,
	}

	err := ctrl.headlineService.Add(headline)
	if err != nil {
		common.Error(c, common.HEADLINE_ADD_ERROR, err.Error())
		return
	}

	common.Ok(c, nil)
}

// FindDetail 查看头条详情
func (ctrl *NewsHeadlineController) FindDetail(c *gin.Context) {
	hidStr := c.Param("hid")
	hid, err := strconv.Atoi(hidStr)
	if err != nil {
		common.Error(c, common.REQUEST_ERROR, "参数错误")
		return
	}

	detailVo, err := ctrl.headlineService.FindHeadlineDetail(hid)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	if detailVo == nil {
		common.Error(c, common.HEADLINE_NOT_EXIST, "该新闻不存在")
		return
	}

	data := map[string]interface{}{
		"headline": detailVo,
	}

	common.Ok(c, data)
}

// FindPage 分页查询
func (ctrl *NewsHeadlineController) FindPage(c *gin.Context) {
	var queryVo vo.HeadlineQueryVo

	// 绑定查询参数，如果失败则使用默认值
	if err := c.ShouldBindQuery(&queryVo); err != nil {
		queryVo.KeyWords = ""
		queryVo.Type = 0
		queryVo.PageNum = 1
		queryVo.PageSize = 5
	}

	// 设置默认值
	if queryVo.PageNum == 0 {
		queryVo.PageNum = 1
	}
	if queryVo.PageSize == 0 {
		queryVo.PageSize = 5
	}

	data, err := ctrl.headlineService.FindPage(&queryVo)
	if err != nil {
		common.Error(c, common.SYSTEM_UNKNOWN_ERROR, err.Error())
		return
	}

	common.Ok(c, data)
}
