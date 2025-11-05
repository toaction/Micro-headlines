import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig(
  () => {
    return{
      plugins: [
        vue(),
        // Element Plus 按需自动导入
        AutoImport({
          resolvers: [ElementPlusResolver()],
        }),
        Components({
          resolvers: [ElementPlusResolver()],
        }),
      ],
      
      server: {
        port: 8001,
        open: true,
        proxy: {
          '/api': {
            target: 'http://localhost:8080/',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      },
      
      // 构建优化配置
      build: {
        // 分块策略
        rollupOptions: {
          output: {
            manualChunks: {
              'vue-vendor': ['vue', 'vue-router', 'pinia'],
              'element-plus': ['element-plus'],
            }
          }
        },
        chunkSizeWarningLimit: 1000,
        sourcemap: false,
        reportCompressedSize: false,
      }
    }
  }
 )
