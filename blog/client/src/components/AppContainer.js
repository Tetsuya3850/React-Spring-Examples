import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import AuthRoute from "./AuthRoute";
import PrivateRoute from "./PrivateRoute";
import AuthNavBar from "./AuthNavBar";
import UnAuthNavBar from "./UnAuthNavBar";
import Feed from "./Feed";
import Hello from "./Hello";
import Signup from "./Signup";
import Signin from "./Signin";
import NewArticle from "./NewArticle";
import User from "./User";
import ArticleDetail from "./ArticleDetail";
import EditArticle from "./EditArticle";
import * as api from "../api";

class AppContainer extends React.Component {
  // Solely for waking up Heroku server, please ignore.
  async componentDidMount() {
    try {
      await api.wakeup();
    } catch (e) {}
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
          <PrivateRoute path="/users/:userId" component={User} />
          <PrivateRoute path="/articles/new" component={NewArticle} />
          <PrivateRoute
            path="/articles/edit/:articleId"
            component={EditArticle}
          />
          <PrivateRoute
            path="/articles/details/:articleId"
            component={ArticleDetail}
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

export default AppContainer;
