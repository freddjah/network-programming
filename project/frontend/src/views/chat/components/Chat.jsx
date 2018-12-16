import React, { Component } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { LoadingMessage, ApplicationError } from '../../messages'

class Chat extends Component {

  constructor(props) {

    super(props)

    this.state = {}
  }

  render() {

    const { isLoading, hasError, errorMessage } = this.props

    if (isLoading) {
      return <LoadingMessage />
    }

    if (hasError) {
      return <ApplicationError>{errorMessage}</ApplicationError>
    }

    return (
      <div>
        <h2>Chat</h2>
      </div>
    )
  }
}

Chat.propTypes = {
  isLoading: PropTypes.bool.isRequired,
  hasError: PropTypes.bool,
  errorMessage: PropTypes.string,
}

Chat.defaultProps = {
  hasError: false,
  errorMessage: '',
}

function mapDispatchToProps(dispatch) {

  return bindActionCreators({
  }, dispatch)
}

function mapStateToProps(applicationState) {

  const state = applicationState.dashboard

  return {
    isLoading: state.get('isLoading'),
    hasError: state.get('hasError'),
    errorMessage: state.get('errorMessage'),
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Chat)
