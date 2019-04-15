import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

import { isAuthed } from "../tokenUtils";
import FollowBtn from "./FollowBtn";

const UserPreview = ({ user, followed, me }) => {
  return (
    <div>
      <div style={styles.container}>
        <Link to={`/users/details/${user.id}`}>{user.username}</Link>
        <FollowBtn me={me} followed={followed} user={user} />
      </div>
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

const mapStateToProps = ({ users, ownInfo }, { userId }) => {
  const user = users[userId] ? users[userId] : {};
  const followed = ownInfo.followingUserIds.includes(userId);
  const me = isAuthed() == userId;
  return {
    user,
    followed,
    me
  };
};

export default connect(
  mapStateToProps,
  null
)(UserPreview);
