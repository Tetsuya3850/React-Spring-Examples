import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleFollow, handleUnfollow } from "../reducers/ownInfoReducer";

const FollowBtn = ({ me, followed, user, handleFollow, handleUnfollow }) => {
  let followBtn = <div />;
  if (!me) {
    followBtn = !followed ? (
      <div onClick={() => handleFollow(user.id)}>Follow</div>
    ) : (
      <div onClick={() => handleUnfollow(user.id)}>Unfollow</div>
    );
  }
  return followBtn;
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFollow, handleUnfollow }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(FollowBtn);
