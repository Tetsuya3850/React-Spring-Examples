import * as api from "../api";
import { incrementHeartCount, decrementHeartCount } from "./tweetsReducer";
import {
  incrementFollowersCount,
  decrementFollowersCount
} from "./usersReducer";

const FETCH_OWNINFO = "FETCH_OWNINFO";
const HEART = "HEART";
const UNHEART = "UNHEART";
const FOLLOW = "FOLLOW";
const UNFOLLOW = "UNFOLLOW";

const fetchOwnInfo = ownInfo => ({
  type: FETCH_OWNINFO,
  ownInfo
});

const heart = tweetId => ({
  type: HEART,
  tweetId
});

const unheart = tweetId => ({
  type: UNHEART,
  tweetId
});

const follow = userId => ({
  type: FOLLOW,
  userId
});

const unfollow = userId => ({
  type: UNFOLLOW,
  userId
});

export const handleFetchOwnInfo = () => async dispatch => {
  try {
    const { data } = await api.getOwnInfo();
    dispatch(fetchOwnInfo(data));
  } catch (error) {
    console.log(error);
  }
};

export const handleHeart = tweetId => async dispatch => {
  try {
    await api.toggleHeart(tweetId);
    dispatch(incrementHeartCount(tweetId));
    dispatch(heart(tweetId));
  } catch (error) {
    console.log(error);
  }
};

export const handleUnheart = tweetId => async dispatch => {
  try {
    await api.toggleHeart(tweetId);
    dispatch(decrementHeartCount(tweetId));
    dispatch(unheart(tweetId));
  } catch (error) {
    console.log(error);
  }
};

export const handleFollow = userId => async dispatch => {
  try {
    await api.toggleFollow(userId);
    dispatch(incrementFollowersCount(userId));
    dispatch(follow(userId));
  } catch (error) {
    console.log(error);
  }
};

export const handleUnfollow = userId => async dispatch => {
  try {
    await api.toggleFollow(userId);
    dispatch(decrementFollowersCount(userId));
    dispatch(unfollow(userId));
  } catch (error) {
    console.log(error);
  }
};

const initialState = {
  followingUserIds: [],
  heartedTweetIds: []
};

const ownInfo = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_OWNINFO:
      return action.ownInfo;
    case HEART:
      return {
        ...state,
        heartedTweetIds: state.heartedTweetIds.concat([action.tweetId])
      };
    case UNHEART:
      return {
        ...state,
        heartedTweetIds: state.heartedTweetIds.filter(
          id => id !== action.tweetId
        )
      };
    case FOLLOW:
      return {
        ...state,
        followingUserIds: state.followingUserIds.concat([action.userId])
      };
    case UNFOLLOW:
      return {
        ...state,
        followingUserIds: state.followingUserIds.filter(
          id => id !== action.userId
        )
      };
    default:
      return state;
  }
};

export default ownInfo;
