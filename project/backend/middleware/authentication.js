const validator = require('validator')

module.exports = ([socket, data]) => new Promise((resolve, reject) => {
  if (typeof socket.nickname !== 'string' || !validator.isAlphanumeric(socket.nickname)) {
    const error = { code: 'AUTH_ERROR', message: 'Authentication failed'}

    return reject(JSON.stringify(error))
  }

  resolve([socket, data])
})