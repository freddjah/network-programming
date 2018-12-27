require('dotenv').config();

const app = require('express')();
const server = require('http').createServer(app);
const io = require('socket.io')(server);

const SocketHandler = require('./net/SocketHandler');
const MiddlewareHandler = require('./middleware/MiddlewareHandler');

const checksumMiddleware = require('./middleware/checksum');
const authMiddleware = require('./middleware/authentication');
const nicknameValidationMiddleware = require('./middleware/nicknameValidation');
const messageValidationMiddleware = require('./middleware/messageValidation');
const md5checksum = require('./common/md5checksum');

const socketHandler = new SocketHandler();

const PORT = process.env.PORT || 3001;

io.on('connection', (socket) => {
  socketHandler.handleInitialConnection({ socket, data: undefined });

  socket.on('nickname', (data) => {
    const ctx = { socket, data };
    const middlewareHandler = new MiddlewareHandler();

    middlewareHandler.use(checksumMiddleware);
    middlewareHandler.use(nicknameValidationMiddleware);

    try {
      middlewareHandler.run(ctx);
      socketHandler.handleNickname(ctx);
    } catch (error) {
      socket.emit('applicationError', md5checksum.createString(JSON.stringify(error)));
    }
  });

  socket.on('newMessage', (data) => {
    const ctx = { socket, data };
    const middlewareHandler = new MiddlewareHandler();

    middlewareHandler.use(authMiddleware);
    middlewareHandler.use(checksumMiddleware);
    middlewareHandler.use(messageValidationMiddleware);

    try {
      middlewareHandler.run(ctx);
      socketHandler.handleNewMessage(ctx);
    } catch (error) {
      socket.emit('applicationError', md5checksum.createString(JSON.stringify(error)));
    }
  });

  socket.on('disconnect', () => {
    socketHandler.handleDisconnect({ socket, data: undefined });
  });
});

server.listen(PORT);

server.on('listening', () => {
  console.log(`✔ Running on port: ${PORT}`);
});

server.on('error', (err) => {
  console.error('✖ Something went wrong. See below:');
  console.error(err);
});
