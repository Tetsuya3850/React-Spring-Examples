import * as api from "./api";

const FETCH_TODOS_REQUEST = "FETCH_TODOS_REQUEST";
const FETCH_TODOS_FAILURE = "FETCH_TODOS_FAILURE";
const FETCH_TODOS_SUCCESS = "FETCH_TODOS_SUCCESS";
const ADD_TODO = "ADD_TODO";
const ADD_TODO_FAILURE = "ADD_TODO_FAILURE";
const DELETE_TODO = "DELETE_TODO";
const DELETE_TODO_FAILURE = "DELETE_TODO_FAILURE";

const fetchTodosRequest = () => ({
  type: FETCH_TODOS_REQUEST
});

const fetchTodosSuccess = todos => ({
  type: FETCH_TODOS_SUCCESS,
  todos
});

const fetchTodosFailure = error => ({
  type: FETCH_TODOS_FAILURE,
  error: "Something went wrong!"
});

export const handleFetchTodos = () => async dispatch => {
  dispatch(fetchTodosRequest());
  try {
    const { data } = await api.fetchTodos();
    dispatch(fetchTodosSuccess(data));
  } catch (error) {
    dispatch(fetchTodosFailure(error));
  }
};

const addTodo = todo => ({
  type: ADD_TODO,
  todo
});

const addTodoFailure = error => ({
  type: ADD_TODO_FAILURE,
  error: "Something went wrong!"
});

export const handleAddTodo = (payload, cleanup) => async dispatch => {
  try {
    const { data } = await api.addTodo(payload);
    dispatch(addTodo(data));
    cleanup();
  } catch (error) {
    dispatch(addTodoFailure(error));
  }
};

const deleteTodo = id => ({
  type: DELETE_TODO,
  id
});

const deleteTodoFailure = error => ({
  type: DELETE_TODO_FAILURE,
  error: "Something went wrong!"
});

export const handleDeleteTodo = id => async dispatch => {
  try {
    await api.deleteTodo(id);
    dispatch(deleteTodo(id));
  } catch (error) {
    dispatch(deleteTodoFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  todos: []
};

const appReducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_TODOS_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_TODOS_SUCCESS:
      return {
        ...state,
        isFetching: false,
        error: "",
        todos: action.todos
      };
    case FETCH_TODOS_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error
      };
    case ADD_TODO:
      return {
        ...state,
        error: "",
        todos: state.todos.concat(action.todo)
      };
    case ADD_TODO_FAILURE:
      return {
        ...state,
        error: action.error
      };
    case DELETE_TODO:
      return {
        ...state,
        error: "",
        todos: state.todos.filter(t => t.id !== action.id)
      };
    case DELETE_TODO_FAILURE:
      return {
        ...state,
        error: action.error
      };
    default:
      return state;
  }
};

export default appReducer;
