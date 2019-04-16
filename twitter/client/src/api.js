import axios from "axios";
import { getToken } from "./tokenUtils";

const SERVER_URL = "https://twitter-server-spring-3850.herokuapp.com";
axios.defaults.baseURL = SERVER_URL;

const setAuthHeader = () => ({
  headers: { authorization: `Bearer ${getToken()}` }
});

export const signup = payload => axios.post(`/users/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = userId => axios.get(`/users/${userId}`, setAuthHeader());
export const getUserList = () => axios.get(`/users`, setAuthHeader());
export const getOwnInfo = () => axios.get(`/users/me`, setAuthHeader());

export const toggleFollow = userId =>
  axios.post(`/follows/${userId}`, null, setAuthHeader());
export const getFollowing = userId =>
  axios.get(`/follows/following/${userId}`, setAuthHeader());
export const getFollowers = userId =>
  axios.get(`/follows/followers/${userId}`, setAuthHeader());

export const toggleHeart = tweetId =>
  axios.post(`/hearts/${tweetId}`, null, setAuthHeader());
export const getHeartedTweets = userId =>
  axios.get(`/hearts/users/${userId}`, setAuthHeader());
export const getHeartedUsers = tweetId =>
  axios.get(`/hearts/tweets/${tweetId}`, setAuthHeader());

export const addTweet = payload =>
  axios.post(`/tweets`, payload, setAuthHeader());
export const getFeed = () => axios.get(`/tweets`, setAuthHeader());
export const getUserFeed = userId =>
  axios.get(`/tweets/users/${userId}`, setAuthHeader());
export const getTweet = tweetId =>
  axios.get(`/tweets/${tweetId}`, setAuthHeader());
export const deleteTweet = tweetId =>
  axios.delete(`/tweets/${tweetId}`, setAuthHeader());
