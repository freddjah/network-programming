import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import { Provider } from 'react-redux'
import { ConnectedRouter } from 'react-router-redux'

import App from './app'
import registerServiceWorker from './registerServiceWorker'
import configureStore from './configureStore'
import routes from './routes'
import SocketClient from './socket/SocketClient'

const socketClient = new SocketClient()
const { store, history } = configureStore(socketClient)

socketClient.init(store)
socketClient.connect()

function init() {

  ReactDOM.render((
    <BrowserRouter>
      <Provider store={store}>
        <ConnectedRouter history={history}>
          <App routes={routes} />
        </ConnectedRouter>
      </Provider>
    </BrowserRouter>
  ), document.getElementById('root'))

  registerServiceWorker()
}

export default init
