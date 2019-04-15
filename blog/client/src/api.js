import axios from "axios";
import { isAuthed, getToken } from "./tokenUtils";

const SERVER_URL = "https://blog-server-spring-3850.herokuapp.com";
axios.defaults.baseURL = SERVER_URL;

if (isAuthed()) {
  axios.defaults.headers.common["authorization"] = `Bearer ${getToken()}`;
}

export const signup = payload => axios.post(`/users/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = userId => axios.get(`/users/${userId}`);

export const addArticle = payload => axios.post(`/articles`, payload);
export const getFeed = page => axios.get(`/articles/?page=${page}`);
export const getUserFeed = userId => axios.get(`/articles/users/${userId}`);
export const getArticle = articleId => axios.get(`/articles/${articleId}`);
export const editArticle = (articleId, payload) =>
  axios.put(`/articles/${articleId}`, payload);
export const deleteArticle = articleId =>
  axios.delete(`/articles/${articleId}`);
