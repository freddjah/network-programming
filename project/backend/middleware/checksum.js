const md5checksum = require('../common/md5checksum')

module.exports = (packet, next) => {
  const [_event, dataJSON] = packet
  let data = null
  
  try {
    data = JSON.parse(dataJSON)
  } catch (e) {
    const error = { code: 'INVALID_JSON', message: 'Data payload was not valid JSON'}

    return next(new Error(JSON.stringify(error)))
  }

  if (typeof data !== 'string') {
    const error = { code: 'INVALID_CHECKSUM', message: 'Data needs to be a string of the structure "[checksum][dataAsJSON]"'}

    return next(new Error(JSON.stringify(error)))
  }

  if (!md5checksum.isValidChecksum(data)) {
    const error = { code: 'INVALID_CHECKSUM', message: 'Checksum did not match the JSON data'}

    return next(new Error(JSON.stringify(error)))
  }

  packet[1] = md5checksum.getData(data)

  next();
}