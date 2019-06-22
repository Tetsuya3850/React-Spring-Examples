import axios from "axios";
import { getToken } from "./tokenUtils";

const SERVER_URL = "https://blog-server-spring-3850.herokuapp.com";
axios.defaults.baseURL = SERVER_URL;

const setAuthHeader = () => ({
  headers: { authorization: `Bearer ${getToken()}` }
});

export const wakeup = () => axios.get("");

export const signup = payload => axios.post(`/persons/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = personId =>
  axios.get(`/persons/${personId}`, setAuthHeader());

export const addArticle = payload =>
  axios.post(`/articles`, payload, setAuthHeader());
export const getFeed = page =>
  axios.get(`/articles/?page=${page}`, setAuthHeader());
export const getUserFeed = personId =>
  axios.get(`/articles/persons/${personId}`, setAuthHeader());
export const getArticle = articleId =>
  axios.get(`/articles/${articleId}`, setAuthHeader());
export const editArticle = (articleId, payload) =>
  axios.put(`/articles/${articleId}`, payload, setAuthHeader());
export const deleteArticle = articleId =>
  axios.delete(`/articles/${articleId}`, setAuthHeader());
