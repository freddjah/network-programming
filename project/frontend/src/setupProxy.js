const proxy = require('http-proxy-middleware') // eslint-disable-line import/no-extraneous-dependencies

module.exports = app => {
  app.use(proxy('/socket.io', { target: 'http://localhost:3001', ws: true }))
}
