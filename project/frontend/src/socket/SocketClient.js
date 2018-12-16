import io from 'socket.io-client'
import { createString, isValidChecksum, getData } from '../md5checksum'
import { pushMessages } from '../views/chat/actions'

function buildRequest(data) {
  return createString(JSON.stringify(data))
}

export default class Client {

  init(store) {
    this.store = store
  }

  connect() {
    this.socket = io.connect('http://localhost:3001')
    this.bindEventListeners()
  }

  setNickname(nickname) {
    this.socket.emit('nickname', buildRequest({ nickname }))
  }

  sendMessage(message) {
    this.socket.emit('newMessage', buildRequest({ message }))
  }

  bindEventListeners() {

    this.socket.on('error', error => {
      console.error('received socket error')
      console.error(error)
    })

    this.socket.on('messages', envelope => {

      if (!isValidChecksum(envelope)) {
        console.error('got message with invalid checksum')
        return
      }

      const { messages } = getData(envelope)
      this.store.dispatch(pushMessages(messages))
    })
  }
}
