const MessageController = require('../controller/MessageController');

const md5checksum = require('../common/md5checksum');

class SocketController {
  constructor() {
    this.messageController = new MessageController();
  }

  handleInitialConnection(ctx) {
    const messages = this.messageController.getAll;
    ctx.socket.emit('messages', md5checksum.createString(JSON.stringify({ messages })));
  }

  handleNewMessage(ctx) {
    const nickname = ctx.socket.nickname;

    const message = this.messageController.addMessage(ctx.data.message, nickname);
    const messagesObject = { messages: [message] };

    ctx.socket.broadcast.emit('messages', md5checksum.createString(JSON.stringify(messagesObject)));
    ctx.socket.emit('messages', md5checksum.createString(JSON.stringify(messagesObject)));
  }

  handleNickname(ctx) {
    ctx.socket.nickname = ctx.data.nickname;
  }

  handleDisconnect(ctx) {
    delete ctx.socket.nickname;
  }
}

module.exports = SocketController;
