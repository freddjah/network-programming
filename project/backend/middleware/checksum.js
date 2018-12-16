const md5checksum = require('../common/md5checksum')

module.exports = (packet, next) => {
  const [_event, dataJSON] = packet
  let data = null
  
  try {
    data = JSON.parse(dataJSON)
  } catch (error) {
    return next(new Error('Data is not valid JSON'))
  }

  if (!md5checksum.isValidChecksum(data)) {
    console.error('Invalid checksum')
    return next(new Error('Invalid checksum'))
  }

  packet[1] = md5checksum.getData(data)

  next();
}