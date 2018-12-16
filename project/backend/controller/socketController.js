const userController = require('./userController')
const messageController = require('./messageController')

const md5checksum = require('../common/md5checksum')

exports.initialConnection = (socket) => {
  const messages = messageController.getAll()
  socket.emit('messages', md5checksum.createString(JSON.stringify(messages)))
}

exports.handleNewMessage = (socket, text) => {
  const user = userController.getUser(socket.id)

  const message = messageController.addMessage(text, user)
  socket.emit('success', 'Successfully added message')

  socket.broadcast.emit('newMessage', md5checksum.createString(JSON.stringify(message)))
  socket.emit('newMessage', md5checksum.createString(JSON.stringify(message)))
}

exports.handleNickname = (socket, username) => {
  userController.addUser(socket.id, username)
  socket.emit('success', `Successfully registered user ${username}`)
}

exports.handleDisconnect = (socket) => {
  userController.removeUser(socket.id)
}