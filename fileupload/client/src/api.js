import axios from "axios";

const SERVER_URL = "http://localhost:8080";
axios.defaults.baseURL = SERVER_URL;

export const upload = payload => axios.post(`/`, payload);
export const getPosts = () => axios.get("/");
