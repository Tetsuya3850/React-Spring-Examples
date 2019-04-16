import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import FollowBtn from "./FollowBtn";

const UserProfile = ({ user }) => (
  <div style={styles.userInfo}>
    <div>Id: {user.username}</div>
    <Link to={`/users/following/${user.id}`}>
      Following: {user.followingCount}
    </Link>
    <Link to={`/users/followers/${user.id}`}>
      Followers: {user.followersCount}
    </Link>
    <Link to={`/users/hearts/${user.id}`}>Hearted Tweets</Link>
    <FollowBtn userId={user.id} />
  </div>
);

const styles = {
  userInfo: {
    display: "flex",
    flexDirection: "column",
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
)(UserProfile);
