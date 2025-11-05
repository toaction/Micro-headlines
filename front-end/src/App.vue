<template>
  <div >
    <Header v-show="isHeader"></Header>
    <router-view></router-view>
  </div>
</template>


<script >
import Header from './components/Header.vue'
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'App'
  })
</script>
<script  setup>
import { computed, onMounted } from "vue"
import { useRoute } from 'vue-router'
import { useUserInfoStore } from './stores/userInfo'
import { getToken } from './utils/token-utils'

const route = useRoute() // 路由信息对象
const userInfoStore = useUserInfoStore()

// 判断是否显示header组件
 const isHeader =  computed(() => {
    return route.name !== "addNews";
})

// 应用初始化时，如果有token则获取用户信息
onMounted(async () => {
  const token = getToken()
  if (token) {
    try {
      await userInfoStore.getInfo()
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
})
  
</script>

<style lang="less" scoped>
  
</style>