import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    // Output to dist/ folder (will be packaged into JAR by Maven)
    outDir: 'dist',
    // Generate source maps for debugging
    sourcemap: true,
  },
  server: {
    port: 3000,
    // Proxy API calls to backend during development
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/setupTests.ts'],
    include: ['test/**/*.{test,spec}.{ts,tsx}'],
    coverage: {
      reporter: ['text', 'lcov'],
      reportsDirectory: './coverage',
    },
  },
})
