import moment from 'moment'
import { setNickname as socketSetNickname, sendMessage as socketSendMessage } from '../../socket/actions'
// import { sendMessage as socketSendMessage } from '../../socketService'

const actionPrefix = '@@chat'

function buildType(subType) {
  return `${actionPrefix}/${subType}`
}

export function pushMessages(messages) {

  return {
    type: buildType('pushMessages'),
    messages,
  }
}

export function sendMessage(message) {

  return dispatch => {

    dispatch(socketSendMessage(message))

    // socketSendMessage(message)
  }

  /*

  return async dispatch => {

    if (showLoadingState) {
      dispatch(setLoadingState(true))
    }

    return new Promise(resolve => {

      // kod för websocket??
      dispatch(setLoadingState(false))
      resolve()
    })
  }
  */
}

export function setLoadingState(isLoading) {

  return {
    type: buildType('setLoadingState'),
    isLoading,
  }
}

export function setError(errorMessage) {

  return {
    type: buildType('setError'),
    errorMessage,
  }
}
