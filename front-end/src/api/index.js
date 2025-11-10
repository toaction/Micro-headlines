import request from "../utils/request/"

//获取分类列表
export const getfindAllTypes = () => {
  return request.get("types");
};
// 分页带条件查询所有头条
export const getfindNewsPageInfo = (info) => {
  return request.get("headlines/page", { params: info });
};
// 查看头条详情
export const getshowHeadlineDetail = (id) => {
  return request.get(`headlines/detail/${id}`);
};

//删除的回调
export const removeByHid = (id) => {
  return request.delete(`headlines/${id}`);
};

//登录的接口
export const getLogin = (info) => {
  return request.post("users/login", info);
};
//获取用户信息的接口
export const getUserInfo = (info) => {
  return request.get("users/info");
};

//注册校验的接口
export const registerValidateApi = (username) => {
  return request.get(`users/check/${username}`);
};

// 注册的接口
export const registerApi = (userInfo) => {
  return request.post("users/register", userInfo)
}

// 修改头条回显的接口
export const getFindHeadlineByHid = (id) => {
  return request.get(`headlines/${id}`);
};

//点击保存修改的回调
export const saveOrAddNews = (news) => {
  return request.put("headlines", news)
}

// 发布头条
export const issueNews = (news) => {
  return request.post("headlines", news)
}

