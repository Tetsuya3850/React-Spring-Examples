import React from "react";
import { NavLink } from "react-router-dom";
import { removeToken } from "../tokenUtils";

const AuthNavBar = ({ history, authedId }) => (
  <div style={styles.container}>
    <NavLink to="/" exact activeStyle={styles.active} style={styles.leftNav}>
      Home
    </NavLink>
    <NavLink
      to={`/users/${authedId}`}
      activeStyle={styles.active}
      style={styles.rightNavs}
    >
      Profile
    </NavLink>
    <NavLink
      to="/articles/new"
      activeStyle={styles.active}
      style={styles.rightNavs}
    >
      New Article
    </NavLink>
    <button
      onClick={() => {
        removeToken();
        history.push("/");
      }}
      style={styles.rightNavs}
    >
      Logout
    </button>
  </div>
);

const styles = {
  container: {
    display: "flex",
    paddingTop: "4px",
    paddingBottom: "4px",
    borderBottom: "1px solid grey"
  },
  active: {
    color: "red"
  },
  leftNav: {
    flexGrow: 4
  },
  rightNavs: {
    flexGrow: 1
  }
};

export default AuthNavBar;
