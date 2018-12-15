let users = {}

exports.create = (id, username) => {
  users[id] = username
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