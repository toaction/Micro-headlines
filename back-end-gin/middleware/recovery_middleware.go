package middleware

import (
	"github.com/action/micro-headlines/common"
	"github.com/gin-gonic/gin"
	"log"
)

// RecoveryMiddleware 全局异常处理中间件
func RecoveryMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		defer func() {
			if err := recover(); err != nil {
				log.Printf("发生异常: %v\n", err)
				common.Error(c, common.SYSTEM_UNKNOWN_ERROR, "系统异常")
				c.Abort()
			}
		}()
		c.Next()
	}
}

