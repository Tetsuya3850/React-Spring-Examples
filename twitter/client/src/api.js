import axios from "axios";
import { getToken } from "./tokenUtils";

const SERVER_URL = "http://localhost:8080";
axios.defaults.baseURL = SERVER_URL;

axios.defaults.headers.common["authorization"] = `Bearer ${getToken()}`;

export const signup = payload => axios.post(`/users/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = userId => axios.get(`/users/${userId}`);
export const getUserList = () => axios.get(`/users`);
export const getOwnInfo = () => axios.get(`/users/me`);

export const toggleFollow = userId => axios.post(`/follows/${userId}`);
export const toggleHeart = tweetId => axios.post(`/hearts/${tweetId}`);

export const addTweet = payload => axios.post(`/tweets`, payload);
export const getFeed = () => axios.get(`/tweets`);
export const getUserFeed = userId => axios.get(`/tweets/users/${userId}`);
export const getTweet = tweetId => axios.get(`/tweets/${tweetId}`);
export const deleteTweet = tweetId => axios.delete(`/tweets/${tweetId}`);
