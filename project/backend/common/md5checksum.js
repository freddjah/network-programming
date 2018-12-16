const md5 = require('md5')

exports.createString = (data) => {
  return `${md5(data)}${data}`
}

exports.isValidChecksum = (checksum, data) => {
  if (!checksum || !data) return false

  return md5(data) === checksum
}