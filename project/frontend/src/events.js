const { sendMessage } = require('./views/chat/actions')

/*
function dispatchEvent(store, message) {

  switch (message.type) {
    case 'sendMessage':
      store.dispatch(sendMessage(message.payload))
      break

    // no default
  }
}
*/

function connect(store) {

  /*
  const socket = new WebSocket(`${window.location.protocol === 'https' ? 'wss' : 'ws'}://${window.location.host}/ws`)

  socket.onmessage = event => {

    const message = JSON.parse(event.data)
    dispatchEvent(store, message)
  }
  */
}

module.exports = {
  connect,
}
