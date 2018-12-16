const actionPrefix = '@@socket'
function buildType(subType) {
  return `${actionPrefix}/${subType}`
}

export function setNickname(nickname) {

  return {
    type: buildType('setNickname'),
    nickname,
  }
}

export function sendMessage(message) {

  return {
    type: buildType('sendMessage'),
    message,
  }
}
