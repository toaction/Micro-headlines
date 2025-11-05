<template>
  <div class="detail-page">
    <div class="detail-container">
      <!-- 返回按钮 -->
      <button class="btn-back" @click="goBack">
        <span class="arrow-left">←</span>
        <span>返回列表</span>
      </button>

      <!-- 文章卡片 -->
      <article class="article-card">
        <!-- 头部信息 -->
        <header class="article-header">
          <div class="category-badge">
            <span class="badge-icon">📰</span>
            <span>{{ detailList.typeName }}</span>
          </div>
          <h1 class="article-title">{{ detailList.title }}</h1>
          <div class="article-meta">
            <div class="meta-item">
              <span class="icon">👁️</span>
              <span>{{ detailList.pageViews }} 次浏览</span>
            </div>
            <div class="meta-divider">|</div>
            <div class="meta-item">
              <span class="icon">🕐</span>
              <span>{{ formatPastTime(detailList.pastHours) }}</span>
            </div>
            <div class="meta-divider">|</div>
            <div class="meta-item">
              <span class="icon">✍️</span>
              <span>作者：{{ detailList.author || '匿名' }}</span>
            </div>
          </div>
        </header>

        <!-- 分割线 -->
        <div class="divider"></div>

        <!-- 文章内容 -->
        <section class="article-content">
          <p class="content-text">{{ detailList.article }}</p>
        </section>

        <!-- 底部装饰 -->
        <footer class="article-footer">
          <div class="footer-decoration">
            <span>— END —</span>
          </div>
        </footer>
      </article>
    </div>
  </div>
</template>

<script >
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'Detail'
  })
</script>
<script  setup>
import { getshowHeadlineDetail } from "../../api/index"
import { ref , onMounted } from "vue"
import { useRoute, useRouter } from 'vue-router'
const route = useRoute() // 路由信息对象
const router = useRouter()

const detailList = ref({}) //详情数据

// 返回列表页
const goBack = () => {
  router.back()
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

//获取详情初始化数据
const getDetailList = async () => {
  let result = await getshowHeadlineDetail(route.query.hid)
  detailList.value = result.headline
}
// 页面初始化钩子
onMounted(() => {
  getDetailList()
})

  
</script>

<style lang="less" scoped>
.detail-page {
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

.detail-container {
  max-width: 900px;
  margin: 0 auto;
}

.btn-back {
  background: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  color: #1890ff;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

  .arrow-left {
    transition: transform 0.3s ease;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(24, 144, 255, 0.2);
    background: #e6f7ff;
    
    .arrow-left {
      transform: translateX(-4px);
    }
  }

  &:active {
    transform: translateY(0);
  }
}

.article-card {
  background: white;
  border-radius: 20px;
  padding: 48px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
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

.article-header {
  text-align: center;
  margin-bottom: 32px;
}

.category-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 24px;
  box-shadow: 0 4px 15px rgba(24, 144, 255, 0.3);

  .badge-icon {
    font-size: 16px;
  }
}

.article-title {
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
  line-height: 1.4;
  margin: 0 0 24px 0;
  letter-spacing: -0.5px;
}

.article-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  color: #7f8c8d;
  font-size: 14px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;

  .icon {
    font-size: 16px;
  }
}

.meta-divider {
  color: #ddd;
}

.divider {
  height: 1px;
  background: linear-gradient(
    to right,
    transparent,
    #e0e0e0 20%,
    #e0e0e0 80%,
    transparent
  );
  margin: 32px 0;
}

.article-content {
  line-height: 2;
  color: #34495e;
  font-size: 16px;
  
  .content-text {
    text-align: justify;
    text-indent: 2em;
    margin: 0;
    white-space: pre-wrap;
    word-wrap: break-word;
  }
}

.article-footer {
  margin-top: 48px;
  text-align: center;
}

.footer-decoration {
  display: inline-block;
  padding: 12px 32px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 20px;
  font-size: 13px;
  color: #7f8c8d;
  font-weight: 600;
  letter-spacing: 2px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-card {
    padding: 24px;
  }

  .article-title {
    font-size: 24px;
  }

  .article-meta {
    flex-direction: column;
    gap: 8px;
  }

  .meta-divider {
    display: none;
  }
}
</style>
