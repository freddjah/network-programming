import React from 'react'
import PropTypes from 'prop-types'

const style = {
  marginTop: '5em',
  width: '100%',
  fontSize: '200%',
  textAlign: 'center',
}

function ApplicationMessage({ children, severity }) {

  let prefix
  let customStyles = {}
  if (severity === 'error') {
    prefix = 'Error: '
    customStyles = { color: '#af000b' }
  }

  return (
    <div style={{ ...style, ...customStyles }}>
      {prefix}
      <span>{children}</span>
    </div>
  )
}

ApplicationMessage.propTypes = {
  children: PropTypes.any.isRequired, // eslint-disable-line react/forbid-prop-types
  severity: PropTypes.string,
}

ApplicationMessage.defaultProps = {
  severity: 'info',
}

export default ApplicationMessage
