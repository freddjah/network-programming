import md5 from 'md5'

export function createString(data) {
  return `${md5(data)}${data}`
}

export function isValidChecksum(string) {

  const md5checksum = string.slice(0, 32)
  const data = string.slice(32)

  if (!md5checksum || !data) return false

  return md5(data) === md5checksum
}

export function getData(string) {

  const data = string.slice(32)
  return JSON.parse(data)
}
