import io from 'socket.io-client'
import { createString, isValidChecksum, getData } from '../utils/md5checksum'
import { pushMessages, setLoadingState, setError } from '../views/chat/actions'

function buildRequest(data) {
  return createString(JSON.stringify(data))
}

function composeErrorMessage(errorCode) {

  switch (errorCode) {

    case 'AUTH_ERROR':
      return 'Something went wrong when authenticating'

    case 'INVALID_CHECKSUM':
    case 'INVALID_JSON':
      return 'Network error between client and server. Please reload the page.'

    default:
      return 'An unknown error has occoured.'
  }
}

export default class Client {

  init(store) {
    this.store = store
  }

  connect() {
    this.socket = io.connect('/')
    this.bindEventListeners()
  }

  setNickname(nickname) {
    this.socket.emit('nickname', buildRequest({ nickname }))
  }

  sendMessage(message) {
    this.socket.emit('newMessage', buildRequest({ message }))
  }

  bindEventListeners() {

    this.socket.on('connect', () => {
      this.store.dispatch(setError(null))
    })

    this.socket.on('connect_error', () => {
      this.store.dispatch(setError('Connection error. Please reload the page.'))
    })

    this.socket.on('applicationError', envelope => {

      const { code } = getData(envelope)
      this.store.dispatch(setError(composeErrorMessage(code)))
    })

    this.socket.on('messages', envelope => {

      if (!isValidChecksum(envelope)) {
        this.store.dispatch(composeErrorMessage('INVALID_CHECKSUM'))
        return
      }

      const { messages } = getData(envelope)
      this.store.dispatch(setLoadingState(false))
      this.store.dispatch(pushMessages(messages))
    })
  }
}
