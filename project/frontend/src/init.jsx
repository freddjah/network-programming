import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import { Provider } from 'react-redux'
import { ConnectedRouter } from 'react-router-redux'

import App from './app'
import registerServiceWorker from './registerServiceWorker'
import store, { createdHistory } from './store'
import routes from './routes'
import events from './events'

events.connect(store)

function init() {

  ReactDOM.render((
    <BrowserRouter>
      <Provider store={store}>
        <ConnectedRouter history={createdHistory}>
          <App routes={routes} />
        </ConnectedRouter>
      </Provider>
    </BrowserRouter>
  ), document.getElementById('root'))

  registerServiceWorker()
}

export default init
