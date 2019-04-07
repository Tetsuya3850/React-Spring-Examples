import axios from "axios";

const SERVER_URL = "http://localhost:8080";
axios.defaults.baseURL = SERVER_URL;

export const fetchTodos = () => axios.get(`/todos/`);
export const addTodo = new_todo => axios.post(`/todos/`, new_todo);
export const deleteTodo = id => axios.delete(`/todos/${id}/`);
