import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'
import chat from './views/chat/reducer'

export default combineReducers({
  chat,
  route: routerReducer,
})
