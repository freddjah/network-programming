const moment = require('moment');

// Saving 10 most recent messages in memory
let messages = [];

class Message {
  constructor(text, nickname) {
    this.message = text;
    this.nickname = nickname;
    this.date = moment().unix();
  }

  save() {
    if (messages.length < 10) {
      messages = [...messages, this];
    } else {
      messages = [...messages.slice(1, 10), this];
    }
  }

  static get getAll() {
    return messages;
  }
}

module.exports = Message;
