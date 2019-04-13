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
    display: "flex"
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
