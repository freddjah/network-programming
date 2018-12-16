const moment = require('moment')

let messages = []

exports.create = (text, nickname) => {
  const message = { text, nickname, date: moment().unix()}

  if (messages.length < 10) {
    messages = [...messages, message]
  } else {
    messages = [...messages.slice(1, 10), message]
  }

  return message
}

exports.getAll = () => {
  return messages
}

