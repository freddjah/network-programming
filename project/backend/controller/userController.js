const User = require('../models/User')

exports.addUser = (id, nickname) => {
  User.create(id, nickname)
}

exports.removeUser = (id) => {
  User.remove(id)
}

exports.getUsers = () => {
  return User.getAll()
}

exports.getUser = (id) => {
  return User.findOne(id)
}