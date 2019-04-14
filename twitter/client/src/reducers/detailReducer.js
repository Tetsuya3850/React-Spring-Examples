import * as api from "../api";
import { addTweets } from "./tweetsReducer";
import { addUsers } from "./usersReducer";
import { normalize } from "normalizr";
import { tweet, user } from "./schema";

const FETCH_DETAIL_REQUEST = "FETCH_DETAIL_REQUEST";
const FETCH_DETAIL_FAILURE = "FETCH_DETAIL_FAILURE";
const FETCH_DETAIL_SUCCESS = "FETCH_DETAIL_SUCCESS";

const fetchDetailRequest = () => ({
  type: FETCH_DETAIL_REQUEST
});

const fetchDetailFailure = error => ({
  type: FETCH_DETAIL_FAILURE,
  error: "Something Went Wrong!"
});

const fetchDetailSuccess = data => ({
  type: FETCH_DETAIL_SUCCESS,
  data
});

export const handleGetTweet = tweetId => async dispatch => {
  dispatch(fetchDetailRequest());
  try {
    const getTweetPromise = api.getTweet(tweetId);
    const getHeartedUserPromise = api.getHeartedUsers(tweetId);
    const [tweetResponse, usersResponse] = await Promise.all([
      getTweetPromise,
      getHeartedUserPromise
    ]);
    const normalizedTweetResponseData = normalize(tweetResponse.data, tweet);
    dispatch(addTweets(normalizedTweetResponseData.entities.tweets));
    dispatch(addUsers(normalizedTweetResponseData.entities.users));
    const normalizedUsersResponseData = normalize(usersResponse.data, [user]);
    dispatch(addUsers(normalizedUsersResponseData.entities.users));
    dispatch(fetchDetailSuccess(normalizedUsersResponseData.result));
  } catch (error) {
    console.log(error);
    dispatch(fetchDetailFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  heartedUsersById: []
};

const detail = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_DETAIL_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_DETAIL_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error
      };
    case FETCH_DETAIL_SUCCESS:
      return {
        ...state,
        isFetching: false,
        error: "",
        heartedUsersById: action.data
      };
    default:
      return state;
  }
};

export default detail;
