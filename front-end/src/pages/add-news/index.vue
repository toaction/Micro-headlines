<template>
  <div class="add-news-page">
    <div class="page-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <span class="title-icon">📝</span>
          <span>发布新闻</span>
        </h1>
        <p class="page-subtitle">分享您的精彩内容</p>
      </div>

      <!-- 表单卡片 -->
      <div class="form-card">
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
                v-for="item in article" 
                :key="item.type"
                :label="item.name" 
                :value="item.type"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        
        <!-- 按钮组 -->
        <div class="button-group">
          <button class="btn btn-cancel" @click="handlerCancel">
            <span class="btn-icon">❌</span>
            <span>取消</span>
          </button>
          <button class="btn btn-save" @click="handlerSave">
            <span class="btn-icon">💾</span>
            <span>发布</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'AddNews'
})
</script>
<script  setup>
import { issueNews } from "../../api/index"
import { ref } from "vue"
import { useRouter } from 'vue-router'
const router = useRouter() 


const formRef = ref()
// 校验规则
const validateType = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章标题是必须的'))
  }
}
// 校验规则
const validateArticle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章内容是必须的'))
  }
}
// 校验规则
const validateTitle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('文章类别是必须的'))
}
}
// 校验规则
const newsRules = {
  title: [{ required: true, trigger: 'blur', validator: validateTitle }],
  article: [{ required: true, trigger: 'blur', validator: validateArticle }],
  type: [{ required: true, validator: validateType }],
}


const formData = ref({
  title: "",   // 文章标题
  article: "", // 文章内容
  type: ""     // 文章类别
})
//初始化文章类别数据
const article = [
  {
    type: "1",
    name: "新闻"
  },
  {
    type: "2",
    name: "体育"
  },
  {
    type: "3",
    name: "娱乐"
  },
  {
    type: "4",
    name: "科技"
  },
  {
    type: "5",
    name: "其他"
  }
]
//点击取消的回调
const handlerCancel = () => {
  router.back()
}
//点击发布的回调
const handlerSave = async () => {
  try {
    await formRef.value?.validate()
    //发送请求
    await issueNews(formData.value)
    ElMessage.success("发布成功")
    router.push({ name: "HeadlineNews" });
  } catch (error) {
    if (error !== false) {
      ElMessage.error("发布失败，请重试")
    }
  }
}


</script>

<style lang="less" scoped>
.add-news-page {
  min-height: 100vh;
  background: #f5f7fa;
  background-image: 
    linear-gradient(30deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(150deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(30deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5),
    linear-gradient(150deg, #f0f2f5 12%, transparent 12.5%, transparent 87%, #f0f2f5 87.5%, #f0f2f5);
  background-size: 80px 140px;
  background-position: 0 0, 0 0, 40px 70px, 40px 70px;
  padding: 40px 20px 60px;
}

.page-container {
  max-width: 900px;
  margin: 0 auto;
  animation: fadeInUp 0.6s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  
  .page-title {
    font-size: 36px;
    font-weight: 700;
    color: #2c3e50;
    margin: 0 0 12px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    
    .title-icon {
      font-size: 40px;
      animation: bounce 2s infinite;
    }
  }
  
  .page-subtitle {
    font-size: 16px;
    color: #7f8c8d;
    margin: 0;
    font-weight: 400;
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.form-card {
  background: white;
  border-radius: 20px;
  padding: 48px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  }
  
  :deep(.el-form) {
    .el-form-item {
      margin-bottom: 32px;
      
      .el-form-item__label {
        font-size: 16px;
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 12px;
        height: auto;
        line-height: 1.5;
      }
      
      .el-input__wrapper {
        border-radius: 12px;
        padding: 12px 18px;
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
        font-size: 15px;
        color: #2c3e50;
        
        &::placeholder {
          color: #bbb;
        }
      }
      
      .el-textarea__inner {
        border-radius: 12px;
        padding: 16px 18px;
        font-size: 15px;
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
        font-size: 13px;
      }
      
      // 下拉选择器样式
      .el-select {
        width: 100%;
      }
    }
  }
}

/* 下拉选项美化 */
:deep(.el-select-dropdown) {
  .el-select-dropdown__item {
    padding: 12px 20px;
    font-size: 15px;
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
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
  padding-top: 32px;
  border-top: 2px solid #f0f0f0;
}

.btn {
  padding: 14px 40px;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  
  .btn-icon {
    font-size: 18px;
    transition: transform 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    
    .btn-icon {
      transform: scale(1.2);
    }
  }
  
  &:active {
    transform: translateY(-1px);
  }
}

.btn-cancel {
  background: linear-gradient(135deg, #e8eaf6 0%, #c5cae9 100%);
  color: #5c6bc0;
  
  &:hover {
    background: linear-gradient(135deg, #c5cae9 0%, #9fa8da 100%);
    box-shadow: 0 8px 25px rgba(92, 107, 192, 0.3);
  }
}

.btn-save {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  
  &:hover {
    background: linear-gradient(135deg, #40a9ff 0%, #1890ff 100%);
    box-shadow: 0 8px 25px rgba(24, 144, 255, 0.4);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .add-news-page {
    padding: 20px 15px 40px;
  }
  
  .page-header {
    margin-bottom: 30px;
    
    .page-title {
      font-size: 28px;
      
      .title-icon {
        font-size: 32px;
      }
    }
    
    .page-subtitle {
      font-size: 14px;
    }
  }
  
  .form-card {
    padding: 24px;
    border-radius: 16px;
  }
  
  .button-group {
    flex-direction: column;
    gap: 12px;
    
    .btn {
      width: 100%;
      justify-content: center;
    }
  }
}

@media (max-width: 480px) {
  .page-header .page-title {
    font-size: 24px;
    flex-direction: column;
    gap: 8px;
  }
  
  .form-card {
    padding: 20px;
  }
}
</style>
