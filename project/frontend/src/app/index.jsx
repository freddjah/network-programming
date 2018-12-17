import React from 'react'
import PropTypes from 'prop-types'
import { Switch } from 'react-router-dom'

function App({ routes }) {

  return (
    <Switch>
      {routes}
    </Switch>
  )
}

App.propTypes = {
  routes: PropTypes.array.isRequired, // eslint-disable-line react/forbid-prop-types
}

export default App
