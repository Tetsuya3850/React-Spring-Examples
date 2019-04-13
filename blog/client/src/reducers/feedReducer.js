import * as api from "../api";
import { addArticles } from "./articlesReducer";
import { addUsers } from "./usersReducer";
import { normalize } from "normalizr";
import { article } from "./schema";

const FETCH_FEED_REQUEST = "FETCH_FEED_REQUEST";
const FETCH_FEED_FAILURE = "FETCH_FEED_FAILURE";
const FETCH_FEED_SUCCESS = "FETCH_FEED_SUCCESS";

const fetchFeedRequest = () => ({
  type: FETCH_FEED_REQUEST
});

const fetchFeedFailure = error => ({
  type: FETCH_FEED_FAILURE,
  error: "Something Went Wrong!"
});

const fetchFeedSuccess = (data, first, last) => ({
  type: FETCH_FEED_SUCCESS,
  data,
  first,
  last
});

export const handleFetchFeed = (page, redirect) => async (
  dispatch,
  getState
) => {
  dispatch(fetchFeedRequest());
  try {
    const { data } = await api.getFeed(page);
    const lastPage =
      data.totalPages > 0 ? data.totalPages - 1 : data.totalPages;
    if (page > lastPage) {
      return redirect(lastPage);
    }
    const normalizedData = normalize(data.content, [article]);
    dispatch(addArticles(normalizedData.entities.articles));
    dispatch(addUsers(normalizedData.entities.users));
    dispatch(fetchFeedSuccess(normalizedData.result, data.first, data.last));
  } catch (error) {
    console.log(error);
    dispatch(fetchFeedFailure(error));
  }
};

const initialState = {
  isFetching: false,
  error: "",
  feedByIds: [],
  first: null,
  last: null
};

const feed = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_FEED_REQUEST:
      return {
        ...state,
        isFetching: true
      };
    case FETCH_FEED_FAILURE:
      return {
        ...state,
        isFetching: false,
        error: action.error,
        first: null,
        last: null
      };
    case FETCH_FEED_SUCCESS:
      return {
        ...state,
        isFetching: false,
        error: "",
        feedByIds: action.data,
        first: action.first,
        last: action.last
      };
    default:
      return state;
  }
};

export default feed;
