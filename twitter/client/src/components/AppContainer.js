import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { BrowserRouter as Router, Route } from "react-router-dom";
import AuthRoute from "./customRoutes/AuthRoute";
import PrivateRoute from "./customRoutes/PrivateRoute";
import AuthNavBar from "./pages/AuthNavBar";
import UnAuthNavBar from "./pages/UnAuthNavBar";
import Feed from "./pages/Feed";
import Hello from "./pages/Hello";
import Signup from "./pages/Signup";
import Signin from "./pages/Signin";
import UserList from "./pages/UserList";
import User from "./pages/User";
import Following from "./pages/Following";
import Followers from "./pages/Followers";
import HeartedTweets from "./pages/HeartedTweets";
import NewTweet from "./pages/NewTweet";
import TweetDetail from "./pages/TweetDetail";
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
