const Message = require('../models/Message')

exports.addMessage = (text, nickname) => {
  return Message.create(text, nickname)
}

exports.getAll = () => {
  return Message.getAll()
}