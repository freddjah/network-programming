const { isValidChecksum } = require('../common/md5checksum')
const validator = require('validator')

module.exports = ([socket, data]) => new Promise((resolve, reject) => {
  const checksum = data.slice(0, 32)

  if (checksum.length < 32) {
    const error = { code: 'INVALID_CHECKSUM', message: 'Data needs to be a string of the structure "[checksum][dataAsJSON]"'}

    return reject(JSON.stringify(error))
  }

  const dataJSON = data.slice(32)

  if (!validator.isJSON(dataJSON)) {
    const error = { code: 'INVALID_JSON', message: 'Data payload was not valid JSON'}

    return reject(JSON.stringify(error))
  }

  if (!isValidChecksum(checksum, dataJSON)) {
    const error = { code: 'INVALID_CHECKSUM', message: 'Checksum did not match the JSON data'}

    return reject(JSON.stringify(error))
  }

  data = JSON.parse(dataJSON)

  resolve([socket, data])
})
