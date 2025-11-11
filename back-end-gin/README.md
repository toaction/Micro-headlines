# Micro Headlines - Gin + GORM 后端项目

这是一个使用 Go 语言、Gin 框架和 GORM ORM 重构的微头条新闻管理系统后端项目。

## 技术栈

- Go 1.20+
- Gin Web 框架
- GORM ORM
- MySQL 数据库
- JWT 认证
- MD5 密码加密

## 项目结构

```
back-end-gin/
├── config/          # 配置管理
├── models/          # 数据模型
├── vo/              # 视图对象
├── dao/             # 数据访问层
├── service/         # 业务逻辑层
├── controller/      # 控制器层
├── middleware/      # 中间件
├── utils/           # 工具包
├── common/          # 通用响应结构
├── config.yaml      # 配置文件
└── main.go          # 主程序入口
```

## 快速开始

### 1. 安装依赖

```bash
go mod tidy
```

### 2. 配置数据库

编辑 `config.yaml` 文件，配置数据库连接信息：

```yaml
database:
  host: localhost
  port: 3306
  username: micro_headlines
  password: "12345678"
  database: micro_headlines
  charset: utf8mb4
```

### 3. 运行项目

```bash
go run main.go
```

服务器将在 `http://localhost:8080` 启动。

## API 接口

### 用户相关

- `GET /users/check/:username` - 检查用户名是否存在
- `POST /users/login` - 用户登录
- `POST /users/register` - 用户注册
- `GET /users/info` - 获取用户信息（需要认证）

### 新闻类型

- `GET /types` - 获取所有新闻类型

### 新闻头条

- `GET /headlines/page` - 分页查询新闻列表
- `GET /headlines/detail/:hid` - 查看新闻详情
- `GET /headlines/:hid` - 根据 ID 查询新闻（需要认证）
- `POST /headlines` - 发布新闻（需要认证）
- `PUT /headlines` - 修改新闻（需要认证）
- `DELETE /headlines/:hid` - 删除新闻（需要认证）

## 认证

需要认证的接口需要在请求头中携带 `token` 字段：

```
token: your_jwt_token_here
```

## 配置说明

### 服务器配置

```yaml
server:
  port: 8080  # 服务器端口
```

### JWT 配置

```yaml
jwt:
  secret_key: micro-headlines  # JWT 密钥
  expiration_time: 3600        # 过期时间（秒）
```

## 注意事项

1. 确保 MySQL 数据库已经创建并且表结构正确
2. 确保 Go 版本为 1.20 或以上
3. 首次运行前请确保配置文件中的数据库信息正确

