const actionPrefix = '@@socket'

export default function middleware(socket) {

  return ({ dispatch, getState }) => next => action => {

    if (typeof action === 'function') {
      return action(dispatch, getState)
    }

    if (!action.type.startsWith(actionPrefix)) {
      return next(action)
    }

    const type = action.type.substring(actionPrefix.length + 1)

    switch (type) {

      case 'setNickname':
        socket.setNickname(action.nickname)
        break

      case 'sendMessage':
        socket.sendMessage(action.message)
        break

      // no default
    }

    return next(action)
  }
}
