const messageController = require('./messageController')

const md5checksum = require('../common/md5checksum')

exports.handleInitialConnection = (socket) => {
  const messages = messageController.getAll()
  socket.emit('messages', md5checksum.createString(JSON.stringify({ messages })))
}

exports.handleNewMessage = (socket, data) => {
  const nickname = socket.nickname

  const message = messageController.addMessage(data.message, nickname)
  const messagesObject = { messages: [message] }

  socket.broadcast.emit('messages', md5checksum.createString(JSON.stringify(messagesObject)))
  socket.emit('messages', md5checksum.createString(JSON.stringify(messagesObject)))
}

exports.handleNickname = (socket, user) => {
  socket.nickname = user.nickname
}

exports.handleDisconnect = (socket) => {
  delete socket.nickname
}