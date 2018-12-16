const userController = require('./userController')
const messageController = require('./messageController')

const md5checksum = require('../common/md5checksum')

exports.handleInitialConnection = (socket) => {
  const messages = messageController.getAll()
  socket.emit('messages', md5checksum.createString(JSON.stringify(messages)))
}

exports.handleNewMessage = (socket, text) => {
  const nickname = socket.nickname

  const message = messageController.addMessage(text, nickname)
  socket.emit('success', 'Successfully added message')

  socket.broadcast.emit('messages', md5checksum.createString(JSON.stringify([message])))
  socket.emit('messages', md5checksum.createString(JSON.stringify([message])))
}

exports.handleNickname = (socket, nickname) => {
  userController.addUser(socket.id, nickname)
  socket.nickname = nickname

  socket.emit('success', `Successfully registered user ${nickname}`)
}

exports.handleDisconnect = (socket) => {
  userController.removeUser(socket.id)
}