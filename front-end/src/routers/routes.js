
export const staticRoutes = [
  {
    path: "/",
    redirect: "/headlines",
  },
  {
    // 头条
    path: "/headlines",
    component: () => import("../pages/headlines/index.vue"),
    name: "HeadlineNews",
  },
  {
    //头条详情
    path: "/headline/detail",
    component: () => import("../pages/detail/index.vue"),
    name: "Detail",
  },
   {
    //发布新闻的页面
    path: "/headline/add",
    component: () => import("../pages/add-news/index.vue"),
    name: "addNews",
  },
];
