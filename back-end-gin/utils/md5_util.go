package utils

import (
	"crypto/md5"
	"encoding/hex"
)

// Encrypt 对字符串进行 MD5 加密
func Encrypt(str string) string {
	h := md5.New()
	h.Write([]byte(str))
	return hex.EncodeToString(h.Sum(nil))
}

