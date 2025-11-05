# 接口文档

## 接口说明

- 基础URL：根据部署环境（Tomcat）配置
- 所有接口后端返回格式统一为JSON

### 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 404 | 未找到 |
| 10001 | 未登录 |
| 10002 | 用户名或密码错误 |
| 10004 | 用户名已被使用 |

---

## 一、主页模块

### 1.1 获取所有新闻类型

**接口功能**：获取系统中所有的新闻类型列表

**URL**：`/index/findAllNewsType`

**请求方式**：GET

**是否需要登录**：否

**请求参数**：无

**返回值**：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "tid": 1,
      "tname": "新闻类型名称"
    }
  ]
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| tid | Integer | 类型ID |
| tname | String | 类型名称 |

---

### 1.2 分页查询头条信息

**接口功能**：根据条件分页查询头条新闻列表，支持关键字搜索和类型筛选

**URL**：`/index/findNewsPage`

**请求方式**：POST

**是否需要登录**：否

**请求参数**：

```json
{
  "keyWords": "搜索关键字",
  "type": 1,
  "pageNum": 1,
  "pageSize": 10
}
```

**请求参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyWords | String | 否 | 搜索关键字，模糊匹配标题 |
| type | Integer | 否 | 新闻类型ID，0表示全部 |
| pageNum | Integer | 是 | 当前页码，从1开始 |
| pageSize | Integer | 是 | 每页记录数 |

**返回值**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "pageInfo": {
      "pageData": [
        {
          "hid": 1,
          "title": "头条标题",
          "type": 1,
          "pageViews": 100,
          "pastHours": 2,
          "publisher": 1
        }
      ],
      "pageNum": 1,
      "pageSize": 10,
      "totalPage": 5,
      "totalSize": 50
    }
  }
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| pageData | Array | 当前页数据列表 |
| hid | Integer | 头条ID |
| title | String | 头条标题 |
| type | Integer | 头条类型 |
| pageViews | Integer | 浏览量 |
| pastHours | Long | 发布距今小时数 |
| publisher | Integer | 发布者ID |
| pageNum | Integer | 当前页码 |
| pageSize | Integer | 每页记录数 |
| totalPage | Integer | 总页数 |
| totalSize | Long | 总记录数 |

---

### 1.3 查看头条详情

**接口功能**：根据头条ID查询头条详细信息，包含作者、类型等完整信息，同时增加浏览量

**URL**：`/index/showHeadlineDetail`

**请求方式**：GET

**是否需要登录**：否

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| hid | Integer | 是 | 头条ID |

**返回值**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "headline": {
      "hid": 1,
      "title": "头条标题",
      "article": "头条内容",
      "type": 1,
      "typeName": "类型名称",
      "pageViews": 101,
      "pastHours": 2,
      "publisher": 1,
      "author": "作者昵称"
    }
  }
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| hid | Integer | 头条ID |
| title | String | 头条标题 |
| article | String | 头条内容 |
| type | Integer | 头条类型ID |
| typeName | String | 头条类型名称 |
| pageViews | Integer | 浏览量 |
| pastHours | Long | 发布距今小时数 |
| publisher | Integer | 发布者ID |
| author | String | 发布者昵称 |

**可能的错误返回**：

```json
{
  "code": 404,
  "message": "not found",
  "data": null
}
```

---

## 二、用户模块

### 2.1 用户注册

**接口功能**：注册新用户账号

**URL**：`/user/register`

**请求方式**：POST

**是否需要登录**：否

**请求参数**：

```json
{
  "username": "用户名",
  "userPwd": "密码",
  "nickName": "昵称"
}
```

**请求参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户登录名 |
| userPwd | String | 是 | 用户密码（明文，后端会加密） |
| nickName | String | 是 | 用户昵称 |

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

失败（用户名已存在）：
```json
{
  "code": 10004,
  "message": "username used",
  "data": null
}
```

---

### 2.2 校验用户名是否已存在

**接口功能**：在用户注册时，校验用户名是否已被使用

**URL**：`/user/checkUserName`

**请求方式**：GET

**是否需要登录**：否

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 要校验的用户名 |

**返回值**：

用户名可用：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

用户名已存在：
```json
{
  "code": 10004,
  "message": "username used",
  "data": null
}
```

---

### 2.3 用户登录

**接口功能**：用户登录，验证成功后返回JWT令牌

**URL**：`/user/login`

**请求方式**：POST

**是否需要登录**：否

**请求参数**：

```json
{
  "username": "用户名",
  "userPwd": "密码"
}
```

**请求参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户登录名 |
| userPwd | String | 是 | 用户密码 |

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

失败：
```json
{
  "code": 10002,
  "message": "name or password error",
  "data": null
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| token | String | JWT令牌，后续需要在请求头中携带此令牌访问需要登录的接口 |

---

### 2.4 检查登录状态

**接口功能**：校验当前token是否有效，用于前端判断用户是否已登录

**URL**：`/user/checkLogin`

**请求方式**：GET

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**：无

**返回值**：

已登录：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

未登录或token无效：
```json
{
  "code": 10001,
  "message": "not login",
  "data": null
}
```

---

### 2.5 获取用户信息

**接口功能**：根据token获取当前登录用户的详细信息

**URL**：`/user/getUserInfo`

**请求方式**：GET

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**：无

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "loginUser": {
      "uid": 1,
      "username": "用户名",
      "nickName": "昵称",
      "userPwd": null
    }
  }
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| uid | Integer | 用户ID |
| username | String | 用户登录名 |
| nickName | String | 用户昵称 |
| userPwd | null | 密码字段已被移除，不返回 |

---

## 三、头条管理模块

**模块说明**：本模块所有接口均需要登录，需在请求头中携带有效的token，否则返回未登录错误。

---

### 3.1 发布头条

**接口功能**：发布一条新的头条新闻

**URL**：`/headline/publish`

**请求方式**：POST

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**：

```json
{
  "title": "头条标题",
  "article": "头条内容",
  "type": 1
}
```

**请求参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 头条标题 |
| article | String | 是 | 头条内容 |
| type | Integer | 是 | 头条类型ID |

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

未登录：
```json
{
  "code": 10001,
  "message": "not login",
  "data": null
}
```

---

### 3.2 根据头条ID查询头条信息

**接口功能**：查询指定头条的信息，用于编辑时回显数据

**URL**：`/headline/findHeadlineByHid`

**请求方式**：GET

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| hid | Integer | 是 | 头条ID |

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "headline": {
      "hid": 1,
      "title": "头条标题",
      "article": "头条内容",
      "type": 1,
      "publisher": 1,
      "pageViews": 100,
      "createTime": "2024-01-01 12:00:00",
      "updateTime": "2024-01-01 12:00:00",
      "isDeleted": 0
    }
  }
}
```

**返回字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| hid | Integer | 头条ID |
| title | String | 头条标题 |
| article | String | 头条内容 |
| type | Integer | 头条类型ID |
| publisher | Integer | 发布者ID |
| pageViews | Integer | 浏览量 |
| createTime | Date | 创建时间 |
| updateTime | Date | 更新时间 |
| isDeleted | Integer | 是否删除（0-未删除，1-已删除） |

---

### 3.3 更新头条

**接口功能**：更新已发布的头条信息

**URL**：`/headline/update`

**请求方式**：POST

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**：

```json
{
  "hid": 1,
  "title": "修改后的标题",
  "article": "修改后的内容",
  "type": 1
}
```

**请求参数说明**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| hid | Integer | 是 | 头条ID |
| title | String | 是 | 头条标题 |
| article | String | 是 | 头条内容 |
| type | Integer | 是 | 头条类型ID |

**返回值**：

成功：
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.4 删除头条

**接口功能**：根据头条ID删除头条（软删除）

**URL**：`/headline/removeByHid`

**请求方式**：POST

**是否需要登录**：是

**请求头**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| token | String | 是 | JWT令牌 |

**请求参数**（URL参数）：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| hid | Integer | 是 | 头条ID |

**返回值**：

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```
---
