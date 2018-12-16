import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'
import dashboard from './views/chat/reducer'

export default combineReducers({
  dashboard,
  route: routerReducer,
})
