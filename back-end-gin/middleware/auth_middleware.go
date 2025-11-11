package middleware

import (
	"github.com/action/micro-headlines/common"
	"github.com/action/micro-headlines/utils"
	"github.com/gin-gonic/gin"
)

// AuthMiddleware 登录验证中间件
func AuthMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		token := c.GetHeader("token")
		
		if token == "" || !utils.ValidateToken(token) {
			common.Unauthorized(c)
			return
		}
		
		userId, err := utils.GetUserIdFromToken(token)
		if err != nil || userId == 0 {
			common.Unauthorized(c)
			return
		}
		
		// 将用户 ID 存入上下文
		utils.SetUserId(c, userId)
		
		c.Next()
	}
}

