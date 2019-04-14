import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { Link } from "react-router-dom";
import { handleFollow, handleUnfollow } from "../reducers/ownInfoReducer";

const UserPreview = ({ user, followed, handleFollow, handleUnfollow }) => (
  <div>
    <div style={styles.container}>
      <Link to={`/users/${user.id}`}>{user.username}</Link>
      {!followed ? (
        <span onClick={() => handleFollow(user.id)}>Follow</span>
      ) : (
        <span onClick={() => handleUnfollow(user.id)}>Unfollow</span>
      )}
    </div>
  </div>
);

const styles = {
  container: {
    display: "flex",
    justifyContent: "space-between",
    paddingTop: "4px",
    paddingBottom: "4px",
    borderBottom: "1px solid grey"
  }
};

const mapStateToProps = ({ users, ownInfo }, { userId }) => {
  const user = users[userId] ? users[userId] : {};
  const followed = ownInfo.followingUserIds.includes(userId);
  return {
    user,
    followed
  };
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFollow, handleUnfollow }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserPreview);
