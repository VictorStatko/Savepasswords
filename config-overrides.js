const path = require('path');

module.exports = {
    paths: function (paths, env) {
        paths.appIndexJs = path.resolve(__dirname, 'app/app.js');
        paths.appSrc = path.resolve(__dirname, 'app');
        return paths;
    },
};