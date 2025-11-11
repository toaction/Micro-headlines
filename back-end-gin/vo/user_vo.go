package vo

// UserRequestVo 用户请求参数（登录和注册共用）
type UserRequestVo struct {
	Username string `json:"username" binding:"required"` // 用户名（必填）
	UserPwd  string `json:"userPwd" binding:"required"`  // 密码（必填）
	NickName string `json:"nickName"`                    // 昵称（可选，仅注册时使用）
}
