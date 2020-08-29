const { createProxyMiddleware } = require('http-proxy-middleware');
const nodejs_port = 5000;

module.exports = function(app) {
    app.use(createProxyMiddleware('/api', {target: `http://127.0.0.1:${nodejs_port}` }));
};