import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  base: "/ourohead/", // 중요: assets 경로가 /ourohead/assets/로 설정됨
  build: {
    outDir: "../backend/src/main/resources/static/ourohead",
    emptyOutDir: true,
  },
  server: {
    proxy: {
      "/ourohead/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});
