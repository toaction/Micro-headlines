package utils

import "github.com/gin-gonic/gin"

const UserIdKey = "userId"

// SetUserId 设置当前请求的用户 ID 到 context
func SetUserId(c *gin.Context, userId int) {
	c.Set(UserIdKey, userId)
}

// GetUserId 从 context 获取当前请求的用户 ID
func GetUserId(c *gin.Context) (int, bool) {
	userId, exists := c.Get(UserIdKey)
	if !exists {
		return 0, false
	}
	
	userIdInt, ok := userId.(int)
	return userIdInt, ok
}

