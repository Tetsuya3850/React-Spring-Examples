import * as api from "../api";
import { addUsers } from "./usersReducer";
import { normalize } from "normalizr";
import { user } from "./schema";

const FETCH_USERLIST_REQUEST = "FETCH_USERLIST_REQUEST";
const FETCH_USERLIST_FAILURE = "FETCH_USERLIST_FAILURE";
const FETCH_USERLIST_SUCCESS = "FETCH_USERLIST_SUCCESS";

const fetchUserListRequest = () => ({
  type: FETCH_USERLIST_REQUEST
});

const fetchUserListFailure = error => ({
  type: FETCH_USERLIST_FAILURE,
  error: "Something Went Wrong!"
});

const fetchUserListSuccess = data => ({
  type: FETCH_USERLIST_SUCCESS,
  data
});

export const handleFetchUserList = () => async dispatch => {
  dispatch(fetchUserListRequest());
  try {
    const { data } = await api.getUserList();
    const normalizedData = normalize(data, [user]);
    dispatch(addUsers(normalizedData.entities.users));
    dispatch(fetchUserListSuccess(normalizedData.result));
  } catch (error) {
    console.log(error);
    dispatch(fetchUserListFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  userListByIds: []
};

const userList = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_USERLIST_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_USERLIST_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error
      };
    case FETCH_USERLIST_SUCCESS:
      return {
        ...state,
        isFetching: false,
        error: "",
        userListByIds: action.data
      };
    default:
      return state;
  }
};

export default userList;
