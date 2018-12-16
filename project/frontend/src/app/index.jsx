import React from 'react'
import PropTypes from 'prop-types'
import { Switch } from 'react-router-dom'

function App({ routes }) {

  return (
    <div style={{ paddingTop: '2em' }}>
      <Switch>
        {routes}
      </Switch>
    </div>
  )
}

App.propTypes = {
  routes: PropTypes.array.isRequired, // eslint-disable-line react/forbid-prop-types
}

export default App
