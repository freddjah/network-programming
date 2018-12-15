require('dotenv').config()

const app = require('express')()
const server = require('http').createServer(app)
const io = require('socket.io')(server)

const checksumMiddleware = require('./middleware/checksum')
const userController = require('./controller/userController')
const messageController = require('./controller/messageController')

const PORT = process.env.PORT || 3000

io.on('connection', (socket) => {
  socket.use(checksumMiddleware)
})

server.listen(PORT)

server.on('listening', () => {
  console.log(`✔ Running on port: ${PORT}`)
})

server.on('error', (err) => {
  console.error(`✖ Something went wrong. See below:`);
  console.error(err)
})
