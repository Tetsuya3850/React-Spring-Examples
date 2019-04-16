import React from "react";
import { NavLink } from "react-router-dom";

const UnAuthNavBar = () => (
  <div style={styles.container}>
    <NavLink to="/" exact activeStyle={styles.active} style={styles.leftNav}>
      Home
    </NavLink>
    <NavLink to="/signup" activeStyle={styles.active} style={styles.rightNavs}>
      Signup
    </NavLink>
    <NavLink to="/signin" activeStyle={styles.active} style={styles.rightNavs}>
      Signin
    </NavLink>
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

export default UnAuthNavBar;
