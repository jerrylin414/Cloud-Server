import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store"

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import("@/views/HomeView.vue"),
    redirect: '/login',
    children: [
      {
        path: "index",
        name: 'Index',
        component: () => import('@/views/Index.vue')
      },
      {
        path: 'filesView',
        name: 'FilesView',
        component: () => import('@/views/FilesView.vue')
      },
      {
        path: 'imageView',
        name: 'ImageView',
        component: () => import('@/views/ImageView.vue')
      },
      {
        path: 'zipFileView',
        name: 'ZipFileView',
        component: () => import('@/views/ZipFile.vue')
      }
    ]
  },
  {
    path: '/login',
    name: "login",
    meta: { title: '登陆页面' },
    component: () => import("@/views/LoginView.vue")
  },
  {
    path: '/register',
    name: "register",
    component: () => import("@/views/Register.vue"),
    meta: { title: '注册页面' }
  },
  {
    path: '/test',
    name: 'test',
    component: () => import("@/views/TestView.vue")
  },
  {
    path:'/share/:code',
    name:'share',
    component: () => import("@/views/ShareView.vue")
  },
  {
    path:'/upload',
    name:'upload',
    component: () => import("@/views/Upload.vue")
  }
]

const router = new VueRouter({
  mode:"history",
  routes
})

// 针对ElementUI导航栏中重复导航报错问题 ---------- 退出登錄 報錯 bugfix
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

router.beforeEach((to, from, next) => {
  if (to.path == '/login' || to.path == '' || to.path == '/' || to.path.includes('/share')) {
    next()
  } else {
    let token = store.state.user.token
    if (token == null || token == '') {
      alert("请重新登陆！");
      return next('/');
    } else {
      next();
    }
  }
})

export default router
