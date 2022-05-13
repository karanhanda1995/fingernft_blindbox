module.exports = {
  productionSourceMap: false,
  devServer: {
    host: "0.0.0.0",
    port: 9528,
    open: false,
    overlay: {
      warnings: false,
      errors: true,
    },
    proxy: {
      "/admin": {
        target: process.env.VUE_APP_API_URL,
        changeOrigin: true,
      },
      "/static": {
        target: process.env.VUE_APP_STATIC_URL,
        changeOrigin: true,
      },
    },
  },
  // 引入全局变量scss
  css: {
    loaderOptions: {
      sass: {
        data: `@import "@/styles/variables.scss";`,
      },
    },
  },
};
