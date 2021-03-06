import { createStore, applyMiddleware, compose } from 'redux'
import createHistory from 'history/createBrowserHistory'
import thunk from 'redux-thunk'
import { routerMiddleware } from 'react-router-redux'
import rootReducer from './reducers'
import socketMiddleware from './socket/reduxMiddleware'

export default function configureStore(socketClient) {

  const history = createHistory()
  const historyMiddleware = routerMiddleware(history)

  const initialState = {}
  const enhancers = []
  const middleware = [
    thunk,
    historyMiddleware,
    socketMiddleware(socketClient),
  ]

  if (process.env.NODE_ENV === 'development') {
    const { devToolsExtension } = window

    if (typeof devToolsExtension === 'function') {
      enhancers.push(devToolsExtension())
    }
  }

  const composedEnhancers = compose(
    applyMiddleware(...middleware),
    ...enhancers,
  )

  const store = createStore(
    rootReducer,
    initialState,
    composedEnhancers,
  )

  return {
    store,
    history,
  }
}
