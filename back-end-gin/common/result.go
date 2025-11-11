package common

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type Result struct {
	Code    int         `json:"code"`
	Message string      `json:"message"`
	Data    interface{} `json:"data,omitempty"`
}

// Ok 成功响应
func Ok(c *gin.Context, data interface{}) {
	c.JSON(http.StatusOK, Result{
		Code:    SUCCESS,
		Message: "success",
		Data:    data,
	})
}

// Error 错误响应
func Error(c *gin.Context, code int, message string) {
	c.JSON(http.StatusOK, Result{
		Code:    code,
		Message: message,
	})
}

// Unauthorized 未授权响应
func Unauthorized(c *gin.Context) {
	c.JSON(http.StatusUnauthorized, Result{
		Code:    http.StatusUnauthorized,
		Message: "未授权",
	})
	c.Abort()
}

