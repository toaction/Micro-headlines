<template>
  <div class="headerContainer">
    <!-- 头部左侧区域 -->
    <div class="left">
      <ul>
        <li @click="HighlightHandler(index,)"  v-for="(item,index) in findAllTypeList" :key="item.tid">
          <a :class="{ active: item.isHighlight }" href="javascript:;">{{item.tname}}</a>
        </li>
      </ul>
    </div>
    <!-- 头部右侧区域 -->
    <div class="right">
      <div class="rightInput">
        <el-input v-model="keywords" placeholder="🔍 搜索最新头条..." clearable></el-input>
      </div>

  
      <!-- 用户登录以后的展示 -->
      <div class="btn-dropdown">
      <!-- 用户没有登录的时候的展示 -->
     
      <div v-if="nickName" class="user-dropdown-wrapper">
        <el-dropdown>
          <el-button type="primary" class="user-button">
            <span class="user-icon">👤</span>
            <span class="user-name">{{ nickName }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handlerNews">📝 发布新闻</el-dropdown-item>
              <el-dropdown-item>👤 个人中心</el-dropdown-item>
              <el-dropdown-item>📚 浏览记录</el-dropdown-item>
              <el-dropdown-item @click="Logout">🚪 退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
       <div v-else class="containerButton">
          <el-button @click="showLoginDialog">🔐 登录</el-button>
          <el-button @click="showRegisterDialog">✨ 注册</el-button>
        </div>
      
      </div>
    </div>
    
    <!-- 登录对话框 -->
    <el-dialog 
      v-model="loginDialogVisible" 
      width="450px"
      :show-close="true"
      :append-to-body="true"
      :lock-scroll="true"
      :close-on-click-modal="true"
      class="auth-dialog"
    >
      <template #header>
        <div class="dialog-header">
          <h3 class="dialog-title">
            <span class="title-icon">🔐</span>
            <span>用户登录</span>
          </h3>
          <p class="dialog-subtitle">欢迎回来，请登录您的账户</p>
        </div>
      </template>
      
      <el-form :model="loginForm" ref="loginFormRef" :rules="loginRules" size="large" label-position="top">
        <el-form-item label="👤 用户名" prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleLogin"
          ></el-input>
        </el-form-item>
        <el-form-item label="🔒 密码" prop="userPwd">
          <el-input 
            v-model="loginForm.userPwd" 
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter="handleLogin"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <button class="btn btn-text" @click="switchToRegister">还没有账户？立即注册</button>
          <div class="dialog-actions">
            <button class="btn btn-cancel" @click="loginDialogVisible = false">
              <span>取消</span>
            </button>
            <button class="btn btn-primary" @click="handleLogin">
              <span class="btn-icon">✓</span>
              <span>登录</span>
            </button>
          </div>
        </div>
      </template>
    </el-dialog>
    
    <!-- 注册对话框 -->
    <el-dialog 
      v-model="registerDialogVisible" 
      width="450px"
      :show-close="true"
      :append-to-body="true"
      :lock-scroll="true"
      :close-on-click-modal="true"
      class="auth-dialog"
    >
      <template #header>
        <div class="dialog-header">
          <h3 class="dialog-title">
            <span class="title-icon">✨</span>
            <span>用户注册</span>
          </h3>
          <p class="dialog-subtitle">加入我们，开启精彩之旅</p>
        </div>
      </template>
      
      <el-form :model="registerForm" ref="registerFormRef" :rules="registerRules" size="large" label-position="top">
        <el-form-item label="✍️ 姓名" prop="nickName">
          <el-input 
            v-model="registerForm.nickName" 
            placeholder="请输入姓名（2-6位）"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="👤 用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名（至少4位）"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="🔒 密码" prop="userPwd">
          <el-input 
            v-model="registerForm.userPwd" 
            type="password"
            placeholder="请输入密码（至少6位）"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="🔑 确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password"
            placeholder="请再次输入密码"
            show-password
          ></el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <button class="btn btn-text" @click="switchToLogin">已有账户？立即登录</button>
          <div class="dialog-actions">
            <button class="btn btn-cancel" @click="registerDialogVisible = false">
              <span>取消</span>
            </button>
            <button class="btn btn-primary" @click="handleRegister">
              <span class="btn-icon">✓</span>
              <span>注册</span>
            </button>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'Header'
})
</script>

<script setup>
import { getfindAllTypes, isUserOverdue, registerValidateApi, registerApi } from '../api/index'
import { ref, onMounted , getCurrentInstance ,watch, computed} from "vue"
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { removeToken } from '../utils/token-utils'
import pinia from '../stores/index';
import { useUserInfoStore } from '../stores/userInfo'
const userInfoStore = useUserInfoStore(pinia)
const nickName = computed(() => userInfoStore.nickName)
// 获取到 全局事件总线
const { Bus } = getCurrentInstance().appContext.config.globalProperties
const router = useRouter()
const keywords = ref("") // 收集搜索最新头条参数

//监视搜索参数的变化 ,当搜索参数变化的时候给HeadlineNews组件传递数据
watch(keywords, (newVal) => {
  Bus.emit('keyword', newVal)
})

const findAllTypeList = ref([])//所有头条分类

// 对话框控制
const loginDialogVisible = ref(false)
const registerDialogVisible = ref(false)
const loginFormRef = ref()
const registerFormRef = ref()

// 登录表单
const loginForm = ref({
  username: "",
  userPwd: ""
})

// 注册表单
const registerForm = ref({
  username: "",
  userPwd: "",
  confirmPassword: "",
  nickName: ""
})

// 登录校验规则
const validateUsername = (rule, value, callback) => {
  if (value.length < 4) {
    callback(new Error('用户名长度不能小于4位'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value.length < 6) {
    callback(new Error('密码长度不能小于6位'))
  } else {
    callback()
  }
}

const loginRules = {
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }]
}

// 注册校验规则
const validateNickName = (rule, value, callback) => {
  if (value.length >= 2 && value.length <= 6) {
    callback()
  } else {
    callback(new Error('姓名必须在2-6位'))
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value.length < 6) {
    callback(new Error('密码长度不能小于6位'))
  } else if (value !== registerForm.value.userPwd) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  nickName: [{ required: true, trigger: 'blur', validator: validateNickName }],
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
  confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
}

// 显示登录对话框
const showLoginDialog = () => {
  loginDialogVisible.value = true
  // 重置表单
  loginForm.value = {
    username: "",
    userPwd: ""
  }
  loginFormRef.value?.clearValidate()
}

// 显示注册对话框
const showRegisterDialog = () => {
  registerDialogVisible.value = true
  // 重置表单
  registerForm.value = {
    username: "",
    userPwd: "",
    confirmPassword: "",
    nickName: ""
  }
  registerFormRef.value?.clearValidate()
}

// 切换到注册
const switchToRegister = () => {
  loginDialogVisible.value = false
  showRegisterDialog()
}

// 切换到登录
const switchToLogin = () => {
  registerDialogVisible.value = false
  showLoginDialog()
}

// 处理登录
const handleLogin = async () => {
  try {
    await loginFormRef.value?.validate()
    await userInfoStore.login(loginForm.value)
    // 登录成功后获取用户信息
    await userInfoStore.getInfo()
    ElMessage.success('登录成功！')
    loginDialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      console.error('登录失败', error)
    }
  }
}

// 处理注册
const handleRegister = async () => {
  try {
    await registerFormRef.value?.validate()
    
    if (registerForm.value.userPwd !== registerForm.value.confirmPassword) {
      ElMessage.error("密码和确认密码必须一致")
      return
    }
    
    // 调用用户名校验接口
    await registerValidateApi(registerForm.value.username)
    
    // 整理参数
    const obj = {
      username: registerForm.value.username,
      userPwd: registerForm.value.userPwd,
      nickName: registerForm.value.nickName
    }
    
    // 调用注册接口
    await registerApi(obj)
    ElMessage.success("注册成功！请登录")
    registerDialogVisible.value = false
    // 自动打开登录对话框
    showLoginDialog()
  } catch (error) {
    if (error !== false) {
      console.error('注册失败', error)
    }
  }
}
const getList = async () => {
  let result = await getfindAllTypes()
  // 遍历数据添加高亮标识
  result.forEach((item) => {
    item.tid = item.tid
    item.tname = item.tname
    item.isHighlight = false
  })
  // 添加微头条数据
  result.unshift({
    isHighlight: true,
    tid: 0,
    tname: "微头条"
  })
  findAllTypeList.value = result
}
// 页面挂载的生命周期回调
onMounted(() => {
  getList()
})

//点击切换高亮的回调(排他思想)
const HighlightHandler = (index) => {
  findAllTypeList.value.forEach((item) => {
    item.isHighlight = false
  })
  // 切换高亮的时候把tid传给HeadlineNews组件
  findAllTypeList.value[index].isHighlight = true
  Bus.emit('tid', findAllTypeList.value[index].tid)
}

// 点击退出登录的回调
const Logout = () => {
  removeToken()
  userInfoStore.initUserInfo()
  router.go({ name: "HeadlineNews" });
}

//点击发布新闻的回调
const handlerNews = async () => {
  //发送请求判断用户是否token过期
  await isUserOverdue()
  router.push({ name: "addNews" });
}
</script>

<style>
.el-dropdown {
  vertical-align: middle;
  min-width: 120px;
}

.el-dropdown+.el-dropdown {
  margin-left: 15px;
}

.el-icon-arrow-down {
  font-size: 12px;
}

/* 下拉菜单美化 */
.el-dropdown-menu {
  border-radius: 12px !important;
  padding: 8px !important;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15) !important;
  border: none !important;
  margin-top: 8px !important;
}

.el-dropdown-menu__item {
  border-radius: 8px !important;
  padding: 12px 20px !important;
  font-size: 14px !important;
  font-weight: 500 !important;
  color: #2c3e50 !important;
  transition: all 0.3s ease !important;
  margin: 4px 0 !important;
}

.el-dropdown-menu__item:hover {
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%) !important;
  color: #1890ff !important;
  transform: translateX(4px) !important;
}

.el-dropdown-menu__item:not(:last-child) {
  border-bottom: 1px solid #f0f0f0;
}
</style>

<style lang="less" scoped>
.headerContainer {
  width: 100%;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 60px 0 40px;
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
  box-sizing: border-box;
  overflow: visible;
  
  .left {
    flex: 1;
    max-width: 60%;
    
    ul {
      display: flex;
      gap: 8px;
      align-items: center;
      
      li {
        list-style: none;
        
        a:-webkit-any-link {
          text-decoration: none;
          color: rgba(255, 255, 255, 0.8);
          font-size: 15px;
          font-weight: 500;
          padding: 10px 20px;
          border-radius: 25px;
          display: inline-block;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          position: relative;
          
          &:hover {
            color: #fff;
            background: rgba(255, 255, 255, 0.15);
            transform: translateY(-2px);
          }
          
          &.active {
            color: #fff;
            background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
            box-shadow: 0 4px 15px rgba(24, 144, 255, 0.4);
            font-weight: 600;
            
            &::after {
              content: '';
              position: absolute;
              bottom: -2px;
              left: 50%;
              transform: translateX(-50%);
              width: 20px;
              height: 3px;
              background: #fff;
              border-radius: 2px;
            }
          }
        }
      }
    }
  }
  
  .right {
    display: flex;
    align-items: center;
    gap: 20px;
    flex-shrink: 0;
    
    .rightInput {
      display: flex;
      align-items: center;
      
      :deep(.el-input) {
        width: 280px;
        
        .el-input__wrapper {
          background: rgba(255, 255, 255, 0.95);
          border-radius: 25px;
          padding: 8px 20px;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;
          border: 2px solid transparent;
          
          &:hover {
            background: #fff;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
          }
          
          &.is-focus {
            border-color: #1890ff;
            box-shadow: 0 6px 25px rgba(24, 144, 255, 0.3);
          }
        }
        
        .el-input__inner {
          height: 36px;
          color: #2c3e50;
          font-size: 14px;
          
          &::placeholder {
            color: #999;
            font-weight: 400;
          }
        }
      }
    }
    
    .btn-dropdown {
      display: flex;
      align-items: center;
      min-width: 140px;
      justify-content: flex-end;
      
      .user-dropdown-wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
      }
      
      .containerButton {
        display: flex;
        align-items: center;
        gap: 12px;
        
        :deep(.el-button) {
          border-radius: 25px;
          padding: 10px 24px;
          font-size: 14px;
          font-weight: 500;
          border: none;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
          
          &:first-child {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: #fff;
            
            &:hover {
              background: linear-gradient(135deg, #34495e 0%, #2c3e50 100%);
              transform: translateY(-2px);
              box-shadow: 0 6px 20px rgba(44, 62, 80, 0.4);
            }
          }
          
          &:last-child {
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
            color: #fff;
            
            &:hover {
              background: linear-gradient(135deg, #ff9800 0%, #ffc107 100%);
              transform: translateY(-2px);
              box-shadow: 0 6px 20px rgba(255, 193, 7, 0.4);
            }
          }
          
          &:active {
            transform: translateY(0);
          }
        }
      }
      
      :deep(.el-dropdown) {
        .el-button {
          background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
          border: none;
          border-radius: 25px;
          padding: 10px 24px;
          font-size: 14px;
          font-weight: 500;
          color: #fff;
          box-shadow: 0 4px 15px rgba(24, 144, 255, 0.3);
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          min-width: auto;
          height: auto;
          line-height: 1.5;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          gap: 6px;
          white-space: nowrap;
          overflow: visible;
          
          &:hover {
            background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(24, 144, 255, 0.4);
          }
          
          &:active {
            transform: translateY(0);
          }
          
          .user-icon {
            font-size: 16px;
            line-height: 1;
            display: inline-flex;
            align-items: center;
          }
          
          .user-name {
            display: inline-block;
            max-width: 100px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .el-icon {
            margin-left: 2px;
            font-size: 12px;
          }
        }
      }
    }
  }
}

.example-showcase .el-dropdown + .el-dropdown {
  margin-left: 15px;
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

/* 登录注册对话框样式 */
.auth-dialog {
  :deep(.el-dialog) {
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  }
  
  :deep(.el-dialog__header) {
    padding: 30px 30px 20px;
    margin: 0;
    border-bottom: 2px solid #f0f0f0;
  }
  
  :deep(.el-dialog__body) {
    padding: 30px;
  }
  
  :deep(.el-dialog__footer) {
    padding: 0 30px 30px;
  }
  
  .dialog-header {
    .dialog-title {
      font-size: 24px;
      font-weight: 700;
      color: #2c3e50;
      margin: 0 0 10px 0;
      display: flex;
      align-items: center;
      gap: 10px;
      
      .title-icon {
        font-size: 28px;
      }
    }
    
    .dialog-subtitle {
      font-size: 14px;
      color: #7f8c8d;
      margin: 0;
      font-weight: 400;
    }
  }
  
  :deep(.el-form) {
    .el-form-item {
      margin-bottom: 24px;
      
      .el-form-item__label {
        font-size: 15px;
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 8px;
        height: auto;
        line-height: 1.5;
      }
      
      .el-input__wrapper {
        border-radius: 12px;
        padding: 10px 16px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        border: 2px solid transparent;
        
        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
        }
        
        &.is-focus {
          border-color: #1890ff;
          box-shadow: 0 4px 16px rgba(24, 144, 255, 0.25);
        }
      }
      
      .el-input__inner {
        font-size: 14px;
        color: #2c3e50;
        
        &::placeholder {
          color: #bbb;
        }
      }
    }
  }
  
  .dialog-footer {
    display: flex;
    flex-direction: column;
    gap: 20px;
    
    .btn-text {
      background: transparent;
      border: none;
      color: #1890ff;
      font-size: 14px;
      cursor: pointer;
      padding: 8px 0;
      transition: all 0.3s ease;
      text-align: center;
      
      &:hover {
        color: #40a9ff;
        text-decoration: underline;
      }
    }
    
    .dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
    }
    
    .btn {
      padding: 12px 32px;
      border: none;
      border-radius: 25px;
      font-size: 15px;
      font-weight: 600;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 8px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      
      .btn-icon {
        font-size: 16px;
        transition: transform 0.3s ease;
      }
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
        
        .btn-icon {
          transform: scale(1.15);
        }
      }
      
      &:active {
        transform: translateY(0);
      }
    }
    
    .btn-cancel {
      background: linear-gradient(135deg, #e8eaf6 0%, #c5cae9 100%);
      color: #5c6bc0;
      
      &:hover {
        background: linear-gradient(135deg, #c5cae9 0%, #9fa8da 100%);
        box-shadow: 0 6px 20px rgba(92, 107, 192, 0.3);
      }
    }
    
    .btn-primary {
      background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
      color: white;
      
      &:hover {
        background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
        box-shadow: 0 6px 20px rgba(24, 144, 255, 0.4);
      }
    }
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .headerContainer {
    padding: 0 30px 0 20px;
    
    .left {
      max-width: 55%;
      
      ul {
        gap: 4px;
        
        li a:-webkit-any-link {
          padding: 8px 16px;
          font-size: 14px;
        }
      }
    }
    
    .right {
      .rightInput :deep(.el-input) {
        width: 200px;
      }
    }
  }
}

@media (max-width: 768px) {
  .headerContainer {
    height: auto;
    min-height: 80px;
    flex-direction: column;
    padding: 15px 20px;
    gap: 15px;
    overflow: visible;
    
    .left {
      width: 100%;
      max-width: 100%;
      
      ul {
        flex-wrap: wrap;
        justify-content: center;
      }
    }
    
    .right {
      width: 100%;
      justify-content: center;
      flex-wrap: wrap;
      
      .rightInput :deep(.el-input) {
        width: 100%;
      }
      
      .btn-dropdown {
        justify-content: center;
      }
    }
  }
  
  .auth-dialog {
    :deep(.el-dialog) {
      width: 90% !important;
    }
    
    .dialog-footer {
      .dialog-actions {
        flex-direction: column;
        
        .btn {
          width: 100%;
          justify-content: center;
        }
      }
    }
  }
}
</style>
