import React from 'react'
import PropTypes from 'prop-types'

function ApplicationError({ children }) {

  return (
    <div className="application-error">
      <h1>{children}</h1>
    </div>
  )
}

ApplicationError.propTypes = {
  children: PropTypes.any.isRequired, // eslint-disable-line react/forbid-prop-types
}

export default ApplicationError
