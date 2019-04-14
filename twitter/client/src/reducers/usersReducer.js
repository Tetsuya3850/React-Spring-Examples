const ADD_USERS = "ADD_USERS";
const INCREMENT_FOLLOWERS_COUNT = "INCREMENT_FOLLOWERS_COUNT";
const DECREMENT_FOLLOWERS_COUNT = "DECREMENT_FOLLOWERS_COUNT";

export const addUsers = users => ({
  type: ADD_USERS,
  users
});

export const incrementFollowersCount = userId => ({
  type: INCREMENT_FOLLOWERS_COUNT,
  userId
});

export const decrementFollowersCount = userId => ({
  type: DECREMENT_FOLLOWERS_COUNT,
  userId
});

const initialState = {};

const users = (state = initialState, action) => {
  switch (action.type) {
    case ADD_USERS:
      return { ...state, ...action.users };
    case INCREMENT_FOLLOWERS_COUNT:
      return {
        ...state,
        [action.userId]: {
          ...state[action.userId],
          followersCount: state[action.userId].followersCount + 1
        }
      };
    case DECREMENT_FOLLOWERS_COUNT:
      return {
        ...state,
        [action.userId]: {
          ...state[action.userId],
          followersCount: state[action.userId].followersCount - 1
        }
      };
    default:
      return state;
  }
};

export default users;
