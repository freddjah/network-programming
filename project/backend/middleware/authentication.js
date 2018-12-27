const validator = require('validator');

module.exports = (ctx, next) => {
  if (ctx.socket.nickname === 'undefined' || typeof ctx.socket.nickname !== 'string' || !validator.isAlphanumeric(ctx.socket.nickname)) {
    const error = new Error('Authentication failed');
    error.code = 'AUTH_ERROR';

    throw error;
  }

  next();
};
