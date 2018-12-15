const User = require('../models/User')

exports.addUser = (id, username) => {
  User.create(id, username)
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