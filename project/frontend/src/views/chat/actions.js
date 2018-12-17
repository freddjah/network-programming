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
