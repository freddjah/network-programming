import React, { Component } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { FormControl, Table } from 'react-bootstrap'
import moment from 'moment'
import { LoadingMessage, ApplicationError } from '../../messages'
import { sendMessage } from '../actions'
import { setNickname } from '../../../socket/actions'

/* eslint-disable react/jsx-one-expression-per-line */

class Chat extends Component {

  constructor(props) {

    super(props)

    this.state = {}

    this.handleChange = this.handleChange.bind(this)
    this.handleKeyUp = this.handleKeyUp.bind(this)

    this.props.setNickname('gunvald')
  }

  handleChange(e) {
    this.setState({ message: e.target.value })
  }

  handleKeyUp(e) {
    if (e.keyCode === 13) {
      this.props.sendMessage(this.state.message)
      this.setState({ message: '' })
    }
  }

  render() {

    const { isLoading, hasError, errorMessage } = this.props
    const { message } = this.state

    /*
    if (isLoading) {
      return <LoadingMessage />
    }

    if (hasError) {
      return <ApplicationError>{errorMessage}</ApplicationError>
    }
    */

    const tableRows = this.props.messages.map(({ nickname, message, date }, key) => {

      const outputTime = moment.unix(date).format('H:mm:ss')

      return (
        <tr key={key}>
          <td>
            <strong>{nickname}:</strong> {message}
            <span style={{ float: 'right' }}><small>{outputTime}</small></span>
          </td>
        </tr>
      )
    })

    return (
      <div>
        <h2>Chat</h2>
        <Table striped bordered condensed>
          <tbody>
            {tableRows}
          </tbody>
        </Table>
        <FormControl type="text" value={message} placeholder="Enter a message..." onChange={this.handleChange} onKeyUp={this.handleKeyUp} />
      </div>
    )
  }
}

Chat.propTypes = {
  isLoading: PropTypes.bool.isRequired,
  sendMessage: PropTypes.func.isRequired,
  setNickname: PropTypes.func.isRequired,
  hasError: PropTypes.bool,
  errorMessage: PropTypes.string,
  messages: PropTypes.arrayOf(PropTypes.object).isRequired,
}

Chat.defaultProps = {
  hasError: false,
  errorMessage: '',
}

function mapDispatchToProps(dispatch) {

  return bindActionCreators({
    sendMessage,
    setNickname,
  }, dispatch)
}

function mapStateToProps(applicationState) {

  const state = applicationState.chat

  return {
    isLoading: state.get('isLoading'),
    hasError: state.get('hasError'),
    errorMessage: state.get('errorMessage'),
    messages: state.get('messages'),
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Chat)
