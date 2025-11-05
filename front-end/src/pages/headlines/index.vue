<template>
  <div class="modern-container">
    <div class="news-list">
      <!-- 每一项头条列表 -->
      <div class="news-card" v-for="item in pageData" :key="item.hid">
        <div class="card-header">
          <span :class="['type-badge', getTypeBadgeClass(item.type)]">
            {{ getTypeName(item.type) }}
          </span>
          <div class="meta-info">
            <span class="views">
              <i class="el-icon-view"></i>
              👁️ {{ item.pageViews }}
            </span>
            <span class="time">
              🕐 {{ formatPastTime(item.pastHours) }}
            </span>
          </div>
        </div>
        
        <div class="card-body">
          <h3 class="news-title" @click="toDetail(item.hid)">{{ item.title }}</h3>
        </div>
        
        <div class="card-footer">
          <button class="btn-detail" @click="toDetail(item.hid)">
            <span>查看详情</span>
            <span class="arrow">→</span>
          </button>
          <div class="action-buttons" v-if="item.publisher == type">
            <button class="btn-edit" @click="Modify(item.hid)">
              ✏️ 编辑
            </button>
            <button class="btn-delete" @click="confirmDelete(item)">
              🗑️ 删除
            </button>
          </div>
        </div>
      </div>
  
      <!-- 分页器 -->
      <div class="pagination-wrapper">
        <el-pagination 
          v-model:current-page="findNewsPageInfo.pageNum"
          v-model:page-size="findNewsPageInfo.pageSize" 
          @size-change="getPageList"
          @current-change="getPageList"
          :page-sizes="[5,7,10]" 
          background
          layout="prev, pager, next , ->, sizes, total" 
          :total="totalSize" />
      </div>
    </div>

    <!-- 修改新闻的侧边抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      direction="rtl"
      size="50%"
      :before-close="handleDrawerClose"
      class="edit-drawer"
    >
      <template #header>
        <div class="drawer-header">
          <h3 class="drawer-title">
            <span class="title-icon">✏️</span>
            <span>编辑新闻</span>
          </h3>
          <p class="drawer-subtitle">修改您的新闻内容</p>
        </div>
      </template>
      
      <div class="drawer-content">
        <el-form :rules="newsRules" :model="formData" ref="formRef" size="large" label-position="top">
          <el-form-item label="📌 文章标题" prop="title">
            <el-input 
              v-model="formData.title" 
              placeholder="请输入一个吸引人的标题..."
              clearable
              maxlength="50"
              show-word-limit
            ></el-input>
          </el-form-item>
          
          <el-form-item label="📄 文章内容" prop="article">
            <el-input 
              v-model="formData.article" 
              type="textarea" 
              :rows="12"
              placeholder="在这里写下您的精彩内容..."
              maxlength="1000"
              show-word-limit
            ></el-input>
          </el-form-item>
          
          <el-form-item label="🏷️ 文章类别" prop="type">
            <el-select v-model="formData.type" placeholder="请选择文章类别" style="width: 100%">
              <el-option 
                v-for="item in articleTypes" 
                :key="item.type" 
                :label="item.name" 
                :value="item.type"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <div class="drawer-footer">
          <button class="btn btn-cancel" @click="handleDrawerClose">
            <span class="btn-icon">❌</span>
            <span>取消</span>
          </button>
          <button class="btn btn-save" @click="handleSave">
            <span class="btn-icon">💾</span>
            <span>保存修改</span>
          </button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script >
import { getfindNewsPageInfo , removeByHid, getFindHeadlineByHid, saveOrAddNews, isUserOverdue } from "../../api/index"
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'HeadlineNews'
  })
</script>
<script  setup>
import { ref, onMounted, getCurrentInstance, watch, computed } from "vue"
import { useRouter } from 'vue-router'
import pinia from '../../stores/index';
import { useUserInfoStore } from '../../stores/userInfo'
const  { Bus } = getCurrentInstance().appContext.config.globalProperties
const userInfoStore = useUserInfoStore(pinia)
const router = useRouter()
const type = computed(() => userInfoStore.uid)

// 抽屉相关状态
const drawerVisible = ref(false)
const formRef = ref()

// 表单数据
const formData = ref({
  hid: null,
  title: "",
  article: "",
  type: ""
})

// 文章类别选项
const articleTypes = [
  { type: "1", name: "新闻" },
  { type: "2", name: "体育" },
  { type: "3", name: "娱乐" },
  { type: "4", name: "科技" },
  { type: "5", name: "其他" }
]

// 表单验证规则
const validateTitle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章标题是必须的'))
  }
}

const validateArticle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章内容是必须的'))
  }
}

const validateType = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章类别是必须的'))
  }
}

const newsRules = {
  title: [{ required: true, trigger: 'blur', validator: validateTitle }],
  article: [{ required: true, trigger: 'blur', validator: validateArticle }],
  type: [{ required: true, validator: validateType }],
}

// 获取类型名称
const getTypeName = (type) => {
  const typeMap = {
    1: '新闻',
    2: '体育',
    3: '娱乐',
    4: '科技',
    5: '其他'
  }
  return typeMap[type] || '其他'
}

// 获取类型徽章样式类名
const getTypeBadgeClass = (type) => {
  const classMap = {
    1: 'badge-news',
    2: 'badge-sports',
    3: 'badge-entertainment',
    4: 'badge-tech',
    5: 'badge-other'
  }
  return classMap[type] || 'badge-other'
}

// 格式化时间显示函数
const formatPastTime = (hours) => {
  if (!hours || hours < 0) return '刚刚'
  
  if (hours < 1) {
    return '1小时内'
  } else if (hours < 24) {
    return `${hours}小时前`
  } else if (hours < 24 * 30) {
    // 小于30天，显示天数和小时
    const days = Math.floor(hours / 24)
    const remainHours = hours % 24
    if (remainHours === 0) {
      return `${days}天前`
    }
    return `${days}天${remainHours}小时前`
  } else if (hours < 24 * 365) {
    // 小于1年，显示月数和天数
    const months = Math.floor(hours / (24 * 30))
    const remainDays = Math.floor((hours % (24 * 30)) / 24)
    if (remainDays === 0) {
      return `${months}个月前`
    }
    return `${months}个月${remainDays}天前`
  } else {
    // 超过1年，显示年数和月数
    const years = Math.floor(hours / (24 * 365))
    const remainMonths = Math.floor((hours % (24 * 365)) / (24 * 30))
    if (remainMonths === 0) {
      return `${years}年前`
    }
    return `${years}年${remainMonths}个月前`
  }
}

const findNewsPageInfo = ref(
  {
    keyWords: "", // 搜索标题关键字
    type: 0,           // 新闻类型
    pageNum: 1,        // 页码数
    pageSize: 5,     // 页大小
  }
)
const totalSize = ref(0) //分页总数量
// 初始化列表数据
const pageData = ref([{
  hid: null,
  pageViews: null,
  pastHours: null,
  publisher: null,
  title: "",
  type: null
}])


//接收header组件用户搜索的数据
Bus.on('keyword', (keywords) => {
  findNewsPageInfo.value.keyWords = keywords
})
// header点击切换高亮的时候传递过来的tid
Bus.on('tid', (type) => {
  findNewsPageInfo.value.type = type
})
// 监视初始化参数type的变化,当type发生改变的时候重新发送请求获取列表数据
watch(() => findNewsPageInfo.value, () => {
  getPageList()
}, {
  deep: true,
})
// 初始化请求分页列表数据
const getPageList = async () => {
  let result = await getfindNewsPageInfo(findNewsPageInfo.value)
  pageData.value = result.pageInfo.pageData
 findNewsPageInfo.value.pageNum = result.pageInfo.pageNum
 findNewsPageInfo.value.pageSize = result.pageInfo.pageSize
 totalSize.value = +result.pageInfo.totalSize
}
// 组件挂载的生命周期钩子
onMounted(() => {
  getPageList()
})
// 点击查看全文的回调
const toDetail = (hid) => {
  router.push({ name: "Detail" ,query:{ hid }});
}

// 确认删除对话框
const confirmDelete = (item) => {
  ElMessageBox.confirm(
    `确定要删除这篇新闻吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      center: true,
      customClass: 'custom-message-box',
      distinguishCancelAndClose: true,
      confirmButtonClass: 'confirm-delete-btn',
      cancelButtonClass: 'cancel-delete-btn',
      dangerouslyUseHTMLString: true,
      message: `
        <div style="text-align: center; padding: 10px 0;">
          <div style="font-size: 16px; color: #303133; margin-bottom: 10px;">
            <strong style="color: #E6A23C;">《${item.title}》</strong>
          </div>
          <div style="font-size: 14px; color: #606266;">
            删除后将无法恢复，请谨慎操作！
          </div>
        </div>
      `
    }
  )
    .then(() => {
      handlerDelete(item.hid)
    })
    .catch((action) => {
      if (action === 'cancel') {
        ElMessage.info('已取消删除')
      }
    })
}

// 点击删除的回调
const handlerDelete = async (id) => {
  try {
    await removeByHid(id)
    ElMessage.success('删除成功!')
    //重新获取列表请求
    getPageList()
  } catch (error) {
    ElMessage.error('删除失败，请重试')
  }
}

//点击修改的回调 - 打开抽屉并加载数据
const Modify = async (hid) => {
  drawerVisible.value = true
  // 重置表单
  formData.value = {
    hid: hid,
    title: "",
    article: "",
    type: ""
  }
  // 获取新闻详情并回显
  try {
    let result = await getFindHeadlineByHid(hid)
    formData.value.title = result.headline.title
    formData.value.article = result.headline.article
    formData.value.type = result.headline.type.toString()
  } catch (error) {
    ElMessage.error('获取新闻详情失败')
  }
}

// 关闭抽屉的回调
const handleDrawerClose = () => {
  // 重置表单验证
  formRef.value?.resetFields()
  drawerVisible.value = false
}

// 保存修改的回调
const handleSave = async () => {
  try {
    // 表单验证
    await formRef.value?.validate()
    // 检查用户登录状态
    await isUserOverdue()
    
    // 准备请求参数
    const params = {
      hid: formData.value.hid,
      title: formData.value.title,
      article: formData.value.article,
      type: formData.value.type
    }
    
    // 发送更新请求
    await saveOrAddNews(params)
    ElMessage.success('修改成功!')
    
    // 关闭抽屉
    drawerVisible.value = false
    
    // 刷新列表
    getPageList()
  } catch (error) {
    if (error.message) {
      // 表单验证失败
      console.log('表单验证失败', error)
    } else {
      ElMessage.error('修改失败，请重试')
    }
  }
}
</script>

<style lang="less" scoped>
.modern-container {
  min-height: 100vh;
  background: #f5f7fa;
  background-image: 
    linear-gradient(30deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(150deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(30deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(150deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5);
  background-size: 80px 140px;
  background-position: 0 0, 0 0, 40px 70px, 40px 70px;
  padding: 40px 20px;
}

.news-list {
  max-width: 900px;
  margin: 0 auto;
}

.news-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 4px;
    height: 100%;
    background: linear-gradient(180deg, #1890ff 0%, #096dd9 100%);
    transition: width 0.3s ease;
  }

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(24, 144, 255, 0.2);
    
    &::before {
      width: 8px;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.type-badge {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;

  &.badge-news {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: white;
  }

  &.badge-sports {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: white;
  }

  &.badge-entertainment {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    color: white;
  }

  &.badge-tech {
    background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
    color: white;
  }

  &.badge-other {
    background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
    color: #666;
  }
}

.meta-info {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;

  span {
    display: flex;
    align-items: center;
    gap: 6px;
  }
}

.card-body {
  margin: 20px 0;
}

.news-title {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  line-height: 1.6;
  margin: 0;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    color: #1890ff;
    transform: translateX(4px);
  }
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-detail {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(24, 144, 255, 0.3);

  .arrow {
    transition: transform 0.3s ease;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(24, 144, 255, 0.4);
    
    .arrow {
      transform: translateX(4px);
    }
  }

  &:active {
    transform: translateY(0);
  }
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.btn-edit, .btn-delete {
  border: none;
  padding: 8px 18px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-edit {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
  color: #8b4513;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(252, 182, 159, 0.4);
  }
}

.btn-delete {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  color: #c41e3a;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(255, 154, 158, 0.4);
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}
</style>

<style>
/* 自定义删除确认框样式 */
.custom-message-box {
  border-radius: 10px !important;
  padding: 20px !important;
  min-width: 400px !important;
}

.custom-message-box .el-message-box__header {
  padding-bottom: 15px;
}

.custom-message-box .el-message-box__title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.custom-message-box .el-message-box__content {
  padding: 20px 15px !important;
}

.custom-message-box .el-message-box__btns {
  padding-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

/* 确定删除按钮样式 */
.confirm-delete-btn {
  background: linear-gradient(135deg, #f5222d 0%, #cf1322 100%) !important;
  border: none !important;
  color: #fff !important;
  padding: 10px 25px !important;
  font-size: 14px !important;
  border-radius: 6px !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
  box-shadow: 0 2px 8px rgba(245, 34, 45, 0.3) !important;
}

.confirm-delete-btn:hover {
  background: linear-gradient(135deg, #ff4d4f 0%, #f5222d 100%) !important;
  box-shadow: 0 4px 12px rgba(245, 34, 45, 0.4) !important;
  transform: translateY(-2px) !important;
}

.confirm-delete-btn:active {
  transform: translateY(0) !important;
}

/* 取消按钮样式 */
.cancel-delete-btn {
  background: #fff !important;
  border: 1px solid #d9d9d9 !important;
  color: #595959 !important;
  padding: 10px 25px !important;
  font-size: 14px !important;
  border-radius: 6px !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
}

.cancel-delete-btn:hover {
  border-color: #40a9ff !important;
  color: #40a9ff !important;
  background: #f0f7ff !important;
}

/* 警告图标样式 */
.custom-message-box .el-message-box__status {
  font-size: 28px !important;
}

/* 成功消息样式 */
.el-message--success {
  border-radius: 8px !important;
  box-shadow: 0 3px 10px rgba(103, 194, 58, 0.2) !important;
}

/* 信息消息样式 */
.el-message--info {
  border-radius: 8px !important;
  box-shadow: 0 3px 10px rgba(144, 147, 153, 0.2) !important;
}

/* 错误消息样式 */
.el-message--error {
  border-radius: 8px !important;
  box-shadow: 0 3px 10px rgba(245, 34, 45, 0.2) !important;
}

/* 编辑抽屉美化样式 */
.edit-drawer {
  .el-drawer__header {
    margin-bottom: 0;
    padding: 30px 30px 20px;
    border-bottom: 2px solid #f0f0f0;
  }
  
  .el-drawer__body {
    padding: 0;
    display: flex;
    flex-direction: column;
  }
  
  .el-drawer__footer {
    padding: 20px 30px;
    border-top: 2px solid #f0f0f0;
  }
}

.drawer-header {
  .drawer-title {
    font-size: 24px;
    font-weight: 700;
    color: #2c3e50;
    margin: 0 0 8px 0;
    display: flex;
    align-items: center;
    gap: 10px;
    
    .title-icon {
      font-size: 28px;
    }
  }
  
  .drawer-subtitle {
    font-size: 14px;
    color: #7f8c8d;
    margin: 0;
    font-weight: 400;
  }
}

.drawer-content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
  
  .el-form {
    .el-form-item {
      margin-bottom: 28px;
      
      .el-form-item__label {
        font-size: 15px;
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 10px;
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
      
      .el-textarea__inner {
        border-radius: 12px;
        padding: 14px 16px;
        font-size: 14px;
        line-height: 1.8;
        color: #2c3e50;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        border: 2px solid transparent;
        resize: vertical;
        
        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
        }
        
        &:focus {
          border-color: #1890ff;
          box-shadow: 0 4px 16px rgba(24, 144, 255, 0.25);
        }
        
        &::placeholder {
          color: #bbb;
        }
      }
      
      .el-input__count {
        color: #999;
        font-size: 12px;
      }
    }
  }
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  
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
  
  .btn-save {
    background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
    color: white;
    
    &:hover {
      background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
      box-shadow: 0 6px 20px rgba(24, 144, 255, 0.4);
    }
  }
}

/* 下拉选项美化 */
.edit-drawer .el-select-dropdown__item {
  padding: 10px 18px;
  font-size: 14px;
  color: #2c3e50;
  transition: all 0.2s ease;
  
  &:hover {
    background: #f0f7ff;
    color: #1890ff;
  }
  
  &.selected {
    color: #1890ff;
    font-weight: 600;
    background: #e6f7ff;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .edit-drawer {
    .drawer-content {
      padding: 20px;
    }
    
    .drawer-footer {
      flex-direction: column;
      
      .btn {
        width: 100%;
        justify-content: center;
      }
    }
  }
}
</style>
