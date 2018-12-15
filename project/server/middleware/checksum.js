module.exports = (packet, next) => {
  const [event, unverifedJSON] = packet
  const unverifiedData = JSON.parse(unverifedJSON)

  if (!md5checksum.isValidChecksum(unverifiedData)) {
    console.error('Invalid checksum')
    return next(new Error('Invalid checksum.'))
  }

  next();
}