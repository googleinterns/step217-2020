const { createProxyMiddleware } = require('http-proxy-middleware');
const nodejs_port = 5000;
const backend_port = 8080;

module.exports = function(app) {
    app.use(createProxyMiddleware('/api', {target: `http://127.0.0.1:${nodejs_port}` }));
    app.use(createProxyMiddleware('/', {target: `http://127.0.0.1:${backend_port}` }));
};