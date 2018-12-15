const md5 = require('md5')

exports.createString = (data) => {
  const dataJSON = JSON.stringify(data)
  return `${md5(dataJSON)} ${dataJSON}`
}

exports.isValidChecksum = (string) => {
  const [md5checksum, data] = string.split(' ')

  if (!md5checksum || !data) return false

  return md5(data) === md5checksum
}

exports.getData = (string) => {
  const [md5checksum, data] = string.split(' ')

  return data
}