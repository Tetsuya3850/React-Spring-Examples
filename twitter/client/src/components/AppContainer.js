import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { BrowserRouter as Router, Route } from "react-router-dom";
import AuthRoute from "./AuthRoute";
import PrivateRoute from "./PrivateRoute";
import AuthNavBar from "./AuthNavBar";
import UnAuthNavBar from "./UnAuthNavBar";
import Feed from "./Feed";
import Hello from "./Hello";
import Signup from "./Signup";
import Signin from "./Signin";
import NewTweet from "./NewTweet";
import UserList from "./UserList";
import User from "./User";
import TweetDetail from "./TweetDetail";
import Following from "./Following";
import Followers from "./Followers";
import HeartedTweets from "./HeartedTweets";
import { isAuthed } from "../tokenUtils";
import { handleFetchOwnInfo } from "../reducers/ownInfoReducer";

class AppContainer extends React.Component {
  componentDidMount() {
    if (isAuthed()) {
      this.props.handleFetchOwnInfo();
    }
  }

  render() {
    return (
      <Router>
        <div style={styles.container}>
          <AuthRoute
            path="/"
            authComponent={AuthNavBar}
            unAuthComponent={UnAuthNavBar}
          />
          <AuthRoute
            exact
            path="/"
            authComponent={Feed}
            unAuthComponent={Hello}
          />
          <Route path="/signup" component={Signup} />
          <Route path="/signin" component={Signin} />

          <PrivateRoute exact path="/users" component={UserList} />
          <PrivateRoute path="/users/details/:userId" component={User} />
          <PrivateRoute path="/users/following/:userId" component={Following} />
          <PrivateRoute path="/users/followers/:userId" component={Followers} />
          <PrivateRoute
            path="/users/hearts/:userId"
            component={HeartedTweets}
          />

          <PrivateRoute path="/tweets/new" component={NewTweet} />
          <PrivateRoute
            path="/tweets/details/:tweetId"
            component={TweetDetail}
          />
        </div>
      </Router>
    );
  }
}

const styles = {
  container: {
    width: 320,
    position: "relative",
    margin: "0 auto"
  }
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFetchOwnInfo }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(AppContainer);
