import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleFollow, handleUnfollow } from "../../reducers/ownInfoReducer";
import { isAuthed } from "../../tokenUtils";

const FollowBtn = ({ me, followed, userId, handleFollow, handleUnfollow }) => {
  let followBtn = <div />;
  if (!me) {
    followBtn = followed ? (
      <button onClick={() => handleUnfollow(userId)}>Unfollow</button>
    ) : (
      <button onClick={() => handleFollow(userId)}>Follow</button>
    );
  }
  return followBtn;
};

const mapStateToProps = ({ ownInfo }, { userId }) => {
  const me = userId === isAuthed();
  const followed = ownInfo.followingUserIds.includes(Number(userId));
  return {
    me,
    followed,
    userId
  };
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFollow, handleUnfollow }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FollowBtn);
