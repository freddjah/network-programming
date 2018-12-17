import React, { Component } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { Button, Col, Grid, FormControl, FormGroup, Panel, Row } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import { LoadingMessage, ApplicationMessage } from '../../messages'
import { setNickname } from '../../../socket/actions'

/* eslint-disable react/jsx-one-expression-per-line */
/* eslint-disable react/no-array-index-key */

class Nickname extends Component {

  constructor(props) {

    super(props)

    this.state = {}

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)

    if ('nickname' in sessionStorage && sessionStorage.nickname.length > 0) {
      this.props.history.push('chat')
    }
  }

  handleChange(e) {
    this.setState({ nickname: e.target.value })
  }

  handleSubmit(e) {

    e.preventDefault()
    this.props.setNickname(this.state.nickname)
    this.props.history.push('chat')
    sessionStorage.nickname = this.state.nickname
  }

  render() {

    const { isLoading, hasError, errorMessage } = this.props
    const { nickname } = this.state

    if (isLoading) {
      return <LoadingMessage />
    }

    if (hasError) {
      return <ApplicationMessage>{errorMessage}</ApplicationMessage>
    }

    return (
      <Grid>
        <Row>
          <Col md={6} mdPush={3}>
            <Panel style={{ marginTop: '5em', textAlign: 'center' }}>
              <Panel.Body style={{ padding: '2em' }}>
                <form onSubmit={this.handleSubmit}>
                  <FormGroup style={{ margin: 0 }}>
                    <FormControl
                      type="text"
                      value={nickname}
                      placeholder="Enter your nickname..."
                      bsSize="large"
                      style={{ textAlign: 'center', marginBottom: '0.5em' }}
                      onChange={this.handleChange}
                    />
                    <Button
                      type="submit"
                      bsStyle="primary"
                      bsSize="large"
                      block
                    >
                      Join chatroom
                    </Button>
                  </FormGroup>
                </form>
              </Panel.Body>
            </Panel>
          </Col>
        </Row>
      </Grid>
    )
  }
}

Nickname.propTypes = {
  isLoading: PropTypes.bool.isRequired,
  setNickname: PropTypes.func.isRequired,
  hasError: PropTypes.bool,
  errorMessage: PropTypes.string,
  history: PropTypes.object.isRequired,
}

Nickname.defaultProps = {
  hasError: false,
  errorMessage: '',
}

function mapDispatchToProps(dispatch) {

  return bindActionCreators({
    setNickname,
  }, dispatch)
}

function mapStateToProps(applicationState) {

  const state = applicationState.chat

  return {
    isLoading: state.get('isLoading'),
    hasError: state.get('hasError'),
    errorMessage: state.get('errorMessage'),
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Nickname))
