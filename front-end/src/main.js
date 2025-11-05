
import { createApp } from 'vue'
import mitt from 'mitt'
import router from "./routers/index";
import App from './App.vue'
import pinia from './stores';

// Element Plus 已通过 unplugin-vue-components 自动按需引入，无需手动导入

const app = createApp(App)
app.config.globalProperties.Bus = mitt()

app.use(pinia)
app.use(router)
app.mount('#app')

