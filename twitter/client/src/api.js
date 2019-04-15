import axios from "axios";
import { isAuthed, getToken } from "./tokenUtils";

const SERVER_URL = "https://twitter-server-spring-3850.herokuapp.com";
axios.defaults.baseURL = SERVER_URL;

if (isAuthed()) {
  axios.defaults.headers.common["authorization"] = `Bearer ${getToken()}`;
}

export const signup = payload => axios.post(`/users/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = userId => axios.get(`/users/${userId}`);
export const getUserList = () => axios.get(`/users`);
export const getOwnInfo = () => axios.get(`/users/me`);

export const toggleFollow = userId => axios.post(`/follows/${userId}`);
export const getFollowing = userId => axios.get(`/follows/following/${userId}`);
export const getFollowers = userId => axios.get(`/follows/followers/${userId}`);

export const toggleHeart = tweetId => axios.post(`/hearts/${tweetId}`);
export const getHeartedTweets = userId => axios.get(`/hearts/users/${userId}`);
export const getHeartedUsers = tweetId =>
  axios.get(`/hearts/tweets/${tweetId}`);

export const addTweet = payload => axios.post(`/tweets`, payload);
export const getFeed = () => axios.get(`/tweets`);
export const getUserFeed = userId => axios.get(`/tweets/users/${userId}`);
export const getTweet = tweetId => axios.get(`/tweets/${tweetId}`);
export const deleteTweet = tweetId => axios.delete(`/tweets/${tweetId}`);
