import request from "../utils/request/"

//获取分类列表
export const getfindAllTypes = () => {
  return request.get("index/findAllNewsType");
};
// 分页带条件查询所有头条
export const getfindNewsPageInfo = (info) => {
  return request.post("index/findNewsPage",info);
};
// 查看头条详情
export const getshowHeadlineDetail = (id) => {
  return request.get("index/showHeadlineDetail", { params: { hid: id } });
};

//删除的回调
// headline/removeByHid
export const removeByHid = (id) => {
  return request.post("headline/removeByHid", null, { params: { hid: id } });
};

//登录的接口
export const getLogin = (info) => {
  return request.post("user/login",info);
};
//获取用户信息的接口
export const getUserInfo = (info) => {
  return request.get("user/getUserInfo");
};

//注册校验的接口  user/checkUserName
export const registerValidateApi = (username) => {
  return request.get("user/checkUserName", { params: { username } });
};

// 注册的接口
export const registerApi = (userInfo) => {
  return request.post("user/register",userInfo)
}
//判断用户登录过期的接口
export const isUserOverdue = () => {
  return request.get("user/checkLogin")
}

// 修改头条回显的接口
export const getFindHeadlineByHid = (id) => {
  return request.get("headline/findHeadlineByHid", { params: { hid: id } });
};

//点击保存修改的回调
// headline/update
export const saveOrAddNews = (news) => {
  return request.post("headline/update",news)
}

// headline/publish
export const issueNews = (news) => {
  return request.post("headline/publish",news)
}

