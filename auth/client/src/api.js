import axios from "axios";
import { getToken } from "./tokenUtils";

const SERVER_URL = "http://localhost:8080";
axios.defaults.baseURL = SERVER_URL;

const setAuthHeader = () => ({
  headers: { authorization: `Bearer ${getToken()}` }
});

export const signup = payload => axios.post(`/users/signup`, payload);
export const signin = payload => axios.post(`/login`, payload);
export const getUser = userId => axios.get(`/users/${userId}`, setAuthHeader());
