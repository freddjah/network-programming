import Immutable from 'immutable'

const actionPrefix = '@@chat'
const initialState = Immutable.fromJS({
  isLoading: true,
  forms: {},
  health: {},
  workers: {},
})

export default (state = initialState, action) => {

  if (!action.type.startsWith(actionPrefix)) {
    return state
  }

  const type = action.type.substring(actionPrefix.length + 1)

  switch (type) {

    case 'pushMessages': {
      return state
    }

    case 'setLoadingState':
      return state.set('isLoading', action.isLoading)

    case 'setError':
      return state.merge(Immutable.fromJS({
        hasError: true,
        errorMessage: action.errorMessage,
      }))

    default:
      return state
  }
}
