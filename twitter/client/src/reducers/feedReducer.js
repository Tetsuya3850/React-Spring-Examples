import * as api from "../api";
import { addTweets } from "./tweetsReducer";
import { addUsers } from "./usersReducer";
import { normalize } from "normalizr";
import { tweet } from "./schema";

const FETCH_FEED_REQUEST = "FETCH_FEED_REQUEST";
const FETCH_FEED_FAILURE = "FETCH_FEED_FAILURE";
const FETCH_FEED_SUCCESS = "FETCH_FEED_SUCCESS";

const fetchFeedRequest = () => ({
  type: FETCH_FEED_REQUEST
});

const fetchFeedFailure = error => ({
  type: FETCH_FEED_FAILURE,
  error: "Something Went Wrong!"
});

const fetchFeedSuccess = data => ({
  type: FETCH_FEED_SUCCESS,
  data
});

export const handleFetchFeed = () => async dispatch => {
  dispatch(fetchFeedRequest());
  try {
    const { data } = await api.getFeed();
    const normalizedData = normalize(data, [tweet]);
    dispatch(addTweets(normalizedData.entities.tweets));
    dispatch(addUsers(normalizedData.entities.users));
    dispatch(fetchFeedSuccess(normalizedData.result));
  } catch (error) {
    console.log(error);
    dispatch(fetchFeedFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  feedByIds: []
};

const feed = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_FEED_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_FEED_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error,
        first: null,
        last: null
      };
    case FETCH_FEED_SUCCESS:
      return {
        ...state,
        isFetching: false,
        error: "",
        feedByIds: action.data
      };
    default:
      return state;
  }
};

export default feed;
