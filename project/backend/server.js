require('dotenv').config()

const app = require('express')()
const server = require('http').createServer(app)
const io = require('socket.io')(server)

const checksumMiddleware = require('./middleware/checksum')
const socketController = require('./controller/socketController')

const PORT = process.env.PORT || 3001

io.on('connection', (socket) => {
  socket.use(checksumMiddleware)

  socketController.initialConnection(socket)

  socket.on('nickname', nickname => socketController.handleNickname(socket, nickname))

  socket.on('newMessage', text => socketController.handleNewMessage(socket, text))

  socket.on('disconnect', () => socketController.handleDisconnect(socket))
})

server.listen(PORT)

server.on('listening', () => {
  console.log(`✔ Running on port: ${PORT}`)
})

server.on('error', (err) => {
  console.error(`✖ Something went wrong. See below:`);
  console.error(err)
})
