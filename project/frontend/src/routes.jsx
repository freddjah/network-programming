import React from 'react'
import { Route } from 'react-router'
import { NotFound } from './views/messages'
import { Chat, Nickname } from './views/chat'

export default [
  <Route key="nickname" path="/" component={Nickname} exact />,
  <Route key="chat" path="/chat" component={Chat} exact />,
  <Route key="notFound" component={NotFound} />,
]
