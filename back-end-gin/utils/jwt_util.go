package utils

import (
	"github.com/action/micro-headlines/config"
	"github.com/golang-jwt/jwt/v5"
	"time"
)

type Claims struct {
	UserId int `json:"userId"`
	jwt.RegisteredClaims
}

// CreateToken 创建 JWT token
func CreateToken(userId int) (string, error) {
	expirationTime := time.Now().Add(time.Duration(config.AppConfig.JWT.ExpirationTime) * time.Second)
	
	claims := &Claims{
		UserId: userId,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(expirationTime),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString([]byte(config.AppConfig.JWT.SecretKey))
	
	return tokenString, err
}

// GetUserIdFromToken 从 token 中获取用户 ID
func GetUserIdFromToken(tokenString string) (int, error) {
	claims := &Claims{}
	
	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(config.AppConfig.JWT.SecretKey), nil
	})
	
	if err != nil || !token.Valid {
		return 0, err
	}
	
	return claims.UserId, nil
}

// ValidateToken 验证 token 是否有效
func ValidateToken(tokenString string) bool {
	if tokenString == "" {
		return false
	}
	
	claims := &Claims{}
	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(config.AppConfig.JWT.SecretKey), nil
	})
	
	return err == nil && token.Valid
}

