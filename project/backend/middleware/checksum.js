const md5checksum = require('../common/md5checksum')

module.exports = (packet, next) => {
  const [_event, dataJSON] = packet
  const data = JSON.parse(dataJSON)

  if (!md5checksum.isValidChecksum(data)) {
    console.error('Invalid checksum')
    return next(new Error('Invalid checksum.'))
  }

  packet[1] = md5checksum.getData(data)

  console.log(packet)

  next();
}