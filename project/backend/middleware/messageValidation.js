

module.exports = (ctx, next) => {
  if (typeof ctx.data.message !== 'string') {
    let error = new Error('Message was empty')
    error.code = 'INVALID_MESSAGE'

    throw error
    
  }

  ctx.data.message = ctx.data.message.trim()

  if (ctx.data.message.length === 0) {
    let error = new Error('Message was empty')
    error.code = 'INVALID_MESSAGE'

    throw error
  }

  next()
}