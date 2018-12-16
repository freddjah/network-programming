let users = {}

exports.create = (id, nickname) => {
  users[id] = nickname
}

exports.remove = (id) => {
  delete users[id]
}

exports.getAll = () => {
  return users
}

exports.findOne = (id) => {
  return users[id]
}