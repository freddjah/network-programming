require('dotenv').config()

const app = require('express')()
const server = require('http').createServer(app)
const io = require('socket.io')(server)

const md5checksum = require('./common/md5checksum')
const checksumMiddleware = require('./middleware/checksum')
const authMiddleware = require('./middleware/authentication')
const messageValidationMiddleware = require('./middleware/message')

const socketController = require('./controller/socketController')

const PORT = process.env.PORT || 3001

io.on('connection', (socket) => {
  socketController.handleInitialConnection(socket)

  socket.on('nickname', data => {
    checksumMiddleware([socket, data])
      .then(socketController.handleNickname)
      .catch(errorJSON => {
        socket.emit('applicationError', md5checksum.createString(errorJSON))
      })
  })

  socket.on('newMessage', data => {
    authMiddleware([socket, data])
      .then(checksumMiddleware)
      .then(messageValidationMiddleware)
      .then(socketController.handleNewMessage)
      .catch(errorJSON => {
        socket.emit('applicationError', md5checksum.createString(errorJSON))
      })
  })
})

server.listen(PORT)

server.on('listening', () => {
  console.log(`✔ Running on port: ${PORT}`)
})

server.on('error', (err) => {
  console.error(`✖ Something went wrong. See below:`);
  console.error(err)
})
