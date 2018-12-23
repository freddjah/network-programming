const { isEmpty, isAlphanumeric } = require('validator')

module.exports = (ctx, next) => {
  if (ctx.data.nickname == undefined) {
    let error = new Error('Nickname was empty')
    error.code = 'AUTH_ERROR'

    throw error
  }

  ctx.data.nickname = ctx.data.nickname.trim()

  if (isEmpty(ctx.data.nickname) || !isAlphanumeric(ctx.data.nickname)) {
    let error = new Error('Nickname was empty')
    error.code = 'AUTH_ERROR'

    throw error
  }

  next()
}
