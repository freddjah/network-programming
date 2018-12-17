const Message = require('../models/Message')

exports.createMessage = (text, nickname) => {
  return Message.create(text, nickname)
}

exports.getAll = () => {
  return Message.getAll()
}