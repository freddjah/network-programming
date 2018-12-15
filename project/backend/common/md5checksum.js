const md5 = require('md5')

exports.createString = (data) => {
  return `${md5(data)}${data}`
}

exports.isValidChecksum = (string) => {
  const md5checksum = string.slice(0, 32)
  const data = string.slice(32)

  if (!md5checksum || !data) return false

  return md5(data) === md5checksum
}

exports.getData = (string) => {
  const data = string.slice(32)

  return data
}