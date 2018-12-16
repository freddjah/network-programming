require('dotenv').config()

const app = require('express')()
const server = require('http').createServer(app)
const io = require('socket.io')(server)

const checksumMiddleware = require('./middleware/checksum')
const userController = require('./controller/userController')
const messageController = require('./controller/messageController')

const md5checksum = require('./common/md5checksum')

const PORT = process.env.PORT || 3001

io.on('connection', (socket) => {
  socket.use(checksumMiddleware)

  socket.on('registerUser', username => {
    userController.addUser(socket.id, username)
    socket.emit('success', `Successfully registered user ${username}`)

    const messages = messageController.getAll()
    socket.emit('updateMessages', md5checksum.createString(JSON.stringify(messages)))
  })

  socket.on('newMessage', text => {
    const user = userController.getUser(socket.id)

    const message = messageController.addMessage(text, user)
    socket.emit('success', 'Successfully added message')

    socket.broadcast.emit('updateMessage', md5checksum.createString(JSON.stringify(message)))
    socket.emit('updateMessage', md5checksum.createString(JSON.stringify(message)))
  })

  socket.on('disconnect', () => {
    userController.removeUser(socket.id)
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
