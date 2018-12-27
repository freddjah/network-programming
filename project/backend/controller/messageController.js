const Message = require('../model/Message');

class MessageController {
  addMessage(text, nickname) {
    const message = new Message(text, nickname);
    message.save();

    return message;
  }

  get getAll() {
    return Message.getAll;
  }
}

module.exports = MessageController;
