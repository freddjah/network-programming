import React, { Component } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { Button, FormControl, Glyphicon, Table } from 'react-bootstrap'
import moment from 'moment'
import { withRouter } from 'react-router-dom'
import { LoadingMessage, ApplicationMessage } from '../../messages'
import { setNickname, sendMessage } from '../../../socket/actions'

/* eslint-disable react/jsx-one-expression-per-line */
/* eslint-disable react/no-array-index-key */

const styles = {
  messageForm: {
    bottom: 0,
    position: 'absolute',
    width: '100%',
  },
  messageInput: {
    borderLeft: 'none',
    borderRight: 'none',
    borderBottom: 'none',
    borderRadius: 0,
  },
  topBar: {
    width: '100%',
    textAlign: 'right',
    backgroundColor: '#666',
  },
  exit: {
    color: '#fff',
  },
  emptyState: {
    marginTop: '5em',
    width: '100%',
    fontSize: '200%',
    textAlign: 'center',
    fontStyle: 'italic',
  },
}

function renderTable(messages) {

  const tableRows = messages.map(({ nickname, message, date }, key) => {

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
    <Table striped bordered condensed>
      <tbody>
        {tableRows}
      </tbody>
    </Table>
  )
}

class Chat extends Component {

  constructor(props) {

    super(props)

    this.state = { message: '' }

    this.handleChange = this.handleChange.bind(this)
    this.handleKeyUp = this.handleKeyUp.bind(this)
    this.exit = this.exit.bind(this)

    if ('nickname' in sessionStorage && sessionStorage.nickname.length > 0) {
      this.props.setNickname(sessionStorage.nickname)
    }
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

  exit() {

    delete sessionStorage.nickname
    this.props.history.push('/')
  }

  render() {

    const { isLoading, hasError, errorMessage } = this.props
    const { message: inputMessage } = this.state

    if (hasError) {
      return <ApplicationMessage severity="error">{errorMessage}</ApplicationMessage>
    }

    if (isLoading) {
      return <LoadingMessage />
    }

    let contents = <div style={styles.emptyState}>The chat is empty</div>
    if (this.props.messages.size > 0) {
      contents = renderTable(this.props.messages)
    }

    return (
      <div>
        <div style={styles.topBar}>
          <Button onClick={this.exit} bsSize="small" bsStyle="link" style={styles.exit}>
            <Glyphicon glyph="log-out" /> Exit chat
          </Button>
        </div>
        {contents}
        <div style={styles.messageForm}>
          <FormControl
            type="text"
            value={inputMessage}
            placeholder="Enter a message..."
            style={styles.messageInput}
            onChange={this.handleChange}
            onKeyUp={this.handleKeyUp}
          />
        </div>
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
  messages: PropTypes.object.isRequired,
  history: PropTypes.object.isRequired,
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

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Chat))
