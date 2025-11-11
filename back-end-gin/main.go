package main

import (
	"fmt"
	"github.com/action/micro-headlines/config"
	"github.com/action/micro-headlines/controller"
	"github.com/action/micro-headlines/dao"
	"github.com/action/micro-headlines/middleware"
	"github.com/action/micro-headlines/service"
	"github.com/gin-gonic/gin"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"
)

func main() {
	// 加载配置
	if err := config.LoadConfig("config.yaml"); err != nil {
		log.Fatalf("加载配置文件失败: %v", err)
	}

	// 初始化数据库连接
	db, err := gorm.Open(mysql.Open(config.AppConfig.Database.GetDSN()), &gorm.Config{})
	if err != nil {
		log.Fatalf("连接数据库失败: %v", err)
	}

	// 初始化 DAO
	userDao := dao.NewNewsUserDao(db)
	typeDao := dao.NewNewsTypeDao(db)
	headlineDao := dao.NewNewsHeadlineDao(db)

	// 初始化 Service
	userService := service.NewNewsUserService(userDao)
	typeService := service.NewNewsTypeService(typeDao)
	headlineService := service.NewNewsHeadlineService(headlineDao)

	// 初始化 Controller
	userController := controller.NewNewsUserController(userService)
	typeController := controller.NewNewsTypeController(typeService)
	headlineController := controller.NewNewsHeadlineController(headlineService)

	// 创建 Gin 引擎
	r := gin.Default()

	// 注册全局中间件
	r.Use(middleware.CORSMiddleware())
	r.Use(middleware.RecoveryMiddleware())

	// 注册路由
	setupRoutes(r, userController, typeController, headlineController)

	// 启动服务器
	addr := fmt.Sprintf(":%d", config.AppConfig.Server.Port)
	log.Printf("服务器启动在 http://localhost%s\n", addr)
	if err := r.Run(addr); err != nil {
		log.Fatalf("服务器启动失败: %v", err)
	}
}

// setupRoutes 设置路由
func setupRoutes(
	r *gin.Engine,
	userController *controller.NewsUserController,
	typeController *controller.NewsTypeController,
	headlineController *controller.NewsHeadlineController,
) {
	// 用户相关路由
	users := r.Group("/users")
	{
		users.GET("/check/:username", userController.CheckUserName)
		users.POST("/login", userController.Login)
		users.POST("/register", userController.Register)
		
		// 需要认证的路由
		users.GET("/info", middleware.AuthMiddleware(), userController.GetUserInfo)
	}

	// 新闻类型路由
	types := r.Group("/types")
	{
		types.GET("", typeController.FindAll)
	}

	// 新闻头条路由
	headlines := r.Group("/headlines")
	{
		// 不需要认证的路由
		headlines.GET("/page", headlineController.FindPage)
		headlines.GET("/detail/:hid", headlineController.FindDetail)
		
		// 需要认证的路由
		authorized := headlines.Group("")
		authorized.Use(middleware.AuthMiddleware())
		{
			authorized.GET("/:hid", headlineController.FindHeadlineByHid)
			authorized.POST("", headlineController.Publish)
			authorized.PUT("", headlineController.Update)
			authorized.DELETE("/:hid", headlineController.Delete)
		}
	}
}

