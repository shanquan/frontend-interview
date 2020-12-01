/**
 * @author: wang.weili
 * @since : 2020/11/25
 */
process.env.VUE_APP_DEV = "http://10.12.5.188:20002"

module.exports = {
    publicPath: "mes3",
    productionSourceMap: false,
    devServer: {
        proxy: {
            '/api/*': {
                target: process.env.VUE_APP_DEV,
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    "^/api": ""
                },
                cookieDomainRewrite: "localhost:8080"
            }
        }
    }
}