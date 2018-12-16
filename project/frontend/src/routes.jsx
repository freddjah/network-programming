import React from 'react'
import { Route } from 'react-router'
import { NotFound } from './views/messages'
import { Chat } from './views/chat'

export default [
  <Route key="chat" path="/" component={Chat} exact />,
  <Route key="notFound" component={NotFound} />,
]
