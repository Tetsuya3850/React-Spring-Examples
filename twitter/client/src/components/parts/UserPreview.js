import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import FollowBtn from "./FollowBtn";

const UserPreview = ({ user }) => {
  return (
    <div style={styles.container}>
      <Link to={`/users/details/${user.id}`}>{user.username}</Link>
      <FollowBtn userId={user.id} />
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    justifyContent: "space-between",
    paddingTop: "4px",
    paddingBottom: "4px",
    borderBottom: "1px solid grey"
  }
};

const mapStateToProps = ({ users }, { userId }) => {
  const user = users[userId] ? users[userId] : {};
  return {
    user
  };
};

export default connect(
  mapStateToProps,
  null
)(UserPreview);
