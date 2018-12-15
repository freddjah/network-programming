let messages = [{username: 'Fredrik', text: 'Hello'}]

exports.create = (text, username) => {
  const message = { text, username }

  if (messages.length < 10) {
    messages = [...messages, message]
  } else {
    messages = [...messages.slice(1, 10), message]
  }

  return message
}

exports.getAll = () => {
  console.log('Model', messages)
  return messages
}

