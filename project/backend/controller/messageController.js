const Message = require('../models/Message')

exports.addMessage = (text, username) => {
  return Message.create(text, username)
}

exports.getAll = () => {
  return Message.getAll()
}