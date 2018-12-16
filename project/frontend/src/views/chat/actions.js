const actionPrefix = '@@chat'

function buildType(subType) {
  return `${actionPrefix}/${subType}`
}

export function sendMessage(showLoadingState) {

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
