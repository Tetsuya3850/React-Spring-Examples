import * as api from "../api";
import { incrementHeartCount, decrementHeartCount } from "./tweetsReducer";
import {
  incrementFollowersCount,
  decrementFollowersCount
} from "./usersReducer";
import { addHeartedUser, removeHeartedUser } from "./detailReducer";

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

export const handleHeart = (tweetId, userId) => async dispatch => {
  try {
    await api.toggleHeart(tweetId);
    dispatch(heart(tweetId));
    dispatch(incrementHeartCount(tweetId));
    dispatch(addHeartedUser(userId));
  } catch (error) {
    console.log(error);
  }
};

export const handleUnheart = (tweetId, userId) => async dispatch => {
  try {
    await api.toggleHeart(tweetId);
    dispatch(unheart(tweetId));
    dispatch(decrementHeartCount(tweetId));
    dispatch(removeHeartedUser(userId));
  } catch (error) {
    console.log(error);
  }
};

export const handleFollow = userId => async dispatch => {
  try {
    await api.toggleFollow(userId);
    dispatch(follow(userId));
    dispatch(incrementFollowersCount(userId));
  } catch (error) {
    console.log(error);
  }
};

export const handleUnfollow = userId => async dispatch => {
  try {
    await api.toggleFollow(userId);
    dispatch(unfollow(userId));
    dispatch(decrementFollowersCount(userId));
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
      return {
        followingUserIds: action.ownInfo.followingPersonIds,
        heartedTweetIds: action.ownInfo.heartedTweetIds
      };
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
