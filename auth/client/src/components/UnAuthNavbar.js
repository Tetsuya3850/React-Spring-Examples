import React from "react";
import { NavLink } from "react-router-dom";

const UnAuthNavBar = () => (
  <div style={styles.container}>
    <NavLink to="/" exact activeStyle={{ color: "red" }} style={styles.leftNav}>
      Home
    </NavLink>
    <NavLink
      to="/signup"
      activeStyle={{ color: "red" }}
      style={styles.rightNavs}
    >
      Signup
    </NavLink>
    <NavLink
      to="/signin"
      activeStyle={{ color: "red" }}
      style={styles.rightNavs}
    >
      Signin
    </NavLink>
  </div>
);

const styles = {
  container: {
    display: "flex"
  },
  leftNav: {
    flexGrow: 4
  },
  rightNavs: {
    flexGrow: 1
  }
};

export default UnAuthNavBar;
