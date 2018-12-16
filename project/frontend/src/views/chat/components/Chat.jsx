import React, { Component } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { Table } from 'react-bootstrap'
import moment from 'moment'
import { LoadingMessage, ApplicationError } from '../../messages'

/* eslint-disable react/jsx-one-expression-per-line */

class Chat extends Component {

  constructor(props) {

    super(props)

    this.state = {}
  }

  render() {

    const { isLoading, hasError, errorMessage } = this.props

    /*
    if (isLoading) {
      return <LoadingMessage />
    }

    if (hasError) {
      return <ApplicationError>{errorMessage}</ApplicationError>
    }
    */

    const messages = [
      {
        username: 'anders',
        text: 'lorem ipsum dolor sit amet, consectetur adipiscing elit',
        time: 1544920607,
      },
      {
        username: 'bengt',
        text: 'sed do eiusmod tempor incididunt ut labore et dolore magna aliqua',
        time: 1544920649,
      },
      {
        username: 'cecilia',
        text: 'ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat',
        time: 1544920652,
      },
    ]

    const tableRows = messages.map(({ username, text, time }) => {

      const outputTime = moment.unix(time).format('h:mm:ss')

      return (
        <tr>
          <td>
            <strong>{username}:</strong> {text}
            <span style={{ float: 'right' }}><small>{outputTime}</small></span>
          </td>
        </tr>
      )
    })

    return (
      <div>
        <h2>Chat</h2>
        <p>
          <Table striped bordered condensed>
            <tbody>
              {tableRows}
            </tbody>
          </Table>
        </p>
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
