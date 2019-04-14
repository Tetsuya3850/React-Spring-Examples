const ADD_TWEETS = "ADD_TWEETS";
const INCREMENT_HEART_COUNT = "INCREMENT_HEART_COUNT";
const DECREMENT_HEART_COUNT = "DECREMENT_HEART_COUNT";

export const addTweets = tweets => ({
  type: ADD_TWEETS,
  tweets
});

export const incrementHeartCount = tweetId => ({
  type: INCREMENT_HEART_COUNT,
  tweetId
});

export const decrementHeartCount = tweetId => ({
  type: DECREMENT_HEART_COUNT,
  tweetId
});

const initialState = {};

const tweets = (state = initialState, action) => {
  switch (action.type) {
    case ADD_TWEETS:
      return { ...state, ...action.tweets };
    case INCREMENT_HEART_COUNT:
      return {
        ...state,
        [action.tweetId]: {
          ...state[action.tweetId],
          heartCount: state[action.tweetId].heartCount + 1
        }
      };
    case DECREMENT_HEART_COUNT:
      return {
        ...state,
        [action.tweetId]: {
          ...state[action.tweetId],
          heartCount: state[action.tweetId].heartCount - 1
        }
      };
    default:
      return state;
  }
};

export default tweets;
