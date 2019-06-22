import axios from "axios";
import { getToken } from "./tokenUtils";

const SERVER_URL = "https://twitter-server-spring-3850.herokuapp.com";
axios.defaults.baseURL = SERVER_URL;

const setAuthHeader = () => ({
  headers: { authorization: `Bearer ${getToken()}` }
});

export const wakeup = () => axios.get("");

export const signup = payload => axios.post(`/persons/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = personId =>
  axios.get(`/persons/${personId}`, setAuthHeader());
export const getUserList = () => axios.get(`/persons`, setAuthHeader());
export const getOwnInfo = () => axios.get(`/persons/me`, setAuthHeader());

export const toggleFollow = personId =>
  axios.post(`/follows/${personId}`, null, setAuthHeader());
export const getFollowing = personId =>
  axios.get(`/follows/following/${personId}`, setAuthHeader());
export const getFollowers = personId =>
  axios.get(`/follows/followers/${personId}`, setAuthHeader());

export const toggleHeart = tweetId =>
  axios.post(`/hearts/${tweetId}`, null, setAuthHeader());
export const getAllHeartedTweets = personId =>
  axios.get(`/hearts/persons/${personId}`, setAuthHeader());
export const getAllHeartedUsers = tweetId =>
  axios.get(`/hearts/tweets/${tweetId}`, setAuthHeader());

export const postTweet = payload =>
  axios.post(`/tweets`, payload, setAuthHeader());
export const getFeed = () => axios.get(`/tweets`, setAuthHeader());
export const getUserFeed = personId =>
  axios.get(`/tweets/persons/${personId}`, setAuthHeader());
export const getTweet = tweetId =>
  axios.get(`/tweets/${tweetId}`, setAuthHeader());
export const deleteTweet = tweetId =>
  axios.delete(`/tweets/${tweetId}`, setAuthHeader());
