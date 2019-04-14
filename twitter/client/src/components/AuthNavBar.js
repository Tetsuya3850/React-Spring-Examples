import React from "react";
import { NavLink } from "react-router-dom";
import { removeToken } from "../tokenUtils";

const AuthNavBar = ({ history, authedId }) => (
  <div style={styles.container}>
    <NavLink to="/" exact activeStyle={styles.active} style={styles.leftNav}>
      Home
    </NavLink>
    <NavLink
      to={`/users/details/${authedId}`}
      exact
      activeStyle={styles.active}
      style={styles.rightNavs}
    >
      Profile
    </NavLink>
    <NavLink
      exact
      to="/users"
      activeStyle={styles.active}
      style={styles.rightNavs}
    >
      UserList
    </NavLink>
    <NavLink
      to="/tweets/new"
      activeStyle={styles.active}
      style={styles.rightNavs}
    >
      New
    </NavLink>
    <div
      onClick={() => {
        removeToken();
        history.push("/");
      }}
      style={{ ...styles.rightNavs, ...styles.logoutBtn }}
    >
      Logout
    </div>
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
  },
  logoutBtn: {
    cursor: "pointer"
  }
};

export default AuthNavBar;
