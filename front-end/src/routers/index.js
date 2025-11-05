import { createRouter, createWebHistory } from "vue-router";
import { staticRoutes } from "./routes";
import { useUserInfoStore } from '../stores/userInfo';
import pinia from '../stores';
import { getToken, removeToken } from '../utils/token-utils';
// ElMessage 已通过自动导入插件引入



const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
});

const userInfoStore = useUserInfoStore(pinia)

//全局前置守卫
router.beforeEach(async (to, from, next) => {
  const token = getToken()
  const userInfo = !!userInfoStore.nickName
  if (token) {
    if (userInfo) {
      next()
    } else {
      try {
        await userInfoStore.getInfo()
        next()
      } catch (error) {
        removeToken()
        next()
      }
    }
  } else {
    next()
  }
});

// //使用全局后置钩子配置关闭进度条
// router.afterEach(() => {
//   NProgress.done();
// });

// 导出路由
export default router;
