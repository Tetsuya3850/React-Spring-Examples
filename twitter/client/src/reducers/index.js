import { combineReducers } from "redux";
import tweets from "./tweetsReducer";
import users from "./usersReducer";
import feed from "./feedReducer";
import userFeed from "./userFeedReducer";
import detail from "./detailReducer";
import userList from "./userListReducer";
import ownInfo from "./ownInfoReducer";

const appReducer = combineReducers({
  tweets,
  users,
  feed,
  userFeed,
  detail,
  userList,
  ownInfo
});

export default appReducer;
