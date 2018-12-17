

module.exports = ([socket, data]) => new Promise((resolve, reject) => {
  if (typeof data.message !== 'string' || data.message.length === 0) {
    const error = { code: 'INVALID_MESSAGE', message: 'Message was empty'}

    return reject(JSON.stringify(error))
  }

  resolve([socket, data])
})