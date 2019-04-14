import * as api from "../api";
import { addTweets } from "./tweetsReducer";
import { addUsers } from "./usersReducer";
import { normalize } from "normalizr";
import { tweet } from "./schema";

const FETCH_USERFEED_REQUEST = "FETCH_USERFEED_REQUEST";
const FETCH_USERFEED_FAILURE = "FETCH_USERFEED_FAILURE";
const FETCH_USERFEED_SUCCESS = "FETCH_USERFEED_SUCCESS";

const fetchUserFeedRequest = () => ({
  type: FETCH_USERFEED_REQUEST
});

const fetchUserFeedFailure = error => ({
  type: FETCH_USERFEED_FAILURE,
  error: "Something Went Wrong!"
});

const fetchUserFeedSuccess = (userId, data) => ({
  type: FETCH_USERFEED_SUCCESS,
  userId,
  data
});

export const handleFetchUserFeed = userId => async dispatch => {
  dispatch(fetchUserFeedRequest());
  try {
    const getUserPromise = api.getUser(userId);
    const getUserFeedPromise = api.getUserFeed(userId);
    const [userResponse, userFeedResponse] = await Promise.all([
      getUserPromise,
      getUserFeedPromise
    ]);
    const normalizedUser = { [userResponse.data.id]: userResponse.data };
    dispatch(addUsers(normalizedUser));
    const normalizedData = normalize(userFeedResponse.data, [tweet]);
    dispatch(addTweets(normalizedData.entities.tweets));
    dispatch(fetchUserFeedSuccess(userId, normalizedData.result));
  } catch (error) {
    console.log(error);
    dispatch(fetchUserFeedFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  userFeedByIds: {}
};

const userFeed = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_USERFEED_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_USERFEED_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error
      };
    case FETCH_USERFEED_SUCCESS:
      return {
        isFetching: false,
        error: "",
        userFeedByIds: {
          ...state.userFeedByIds,
          [action.userId]: action.data
        }
      };
    default:
      return state;
  }
};

export default userFeed;
