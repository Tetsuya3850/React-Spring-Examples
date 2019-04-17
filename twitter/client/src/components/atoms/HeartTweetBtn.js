import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleHeart, handleUnheart } from "../../reducers/ownInfoReducer";
import { isAuthed } from "../../tokenUtils";

const HeartTweetBtn = ({
  hearted,
  tweetId,
  authedId,
  handleHeart,
  handleUnheart
}) => (
  <span>
    {hearted ? (
      <i
        className="fas fa-heart"
        style={styles.btn}
        onClick={() => handleUnheart(tweetId, authedId)}
      />
    ) : (
      <i
        className="far fa-heart"
        style={styles.btn}
        onClick={() => handleHeart(tweetId, authedId)}
      />
    )}
  </span>
);

const styles = {
  btn: {
    cursor: "pointer"
  }
};

const mapStateToProps = ({ ownInfo }, { tweetId }) => {
  const hearted = ownInfo.heartedTweetIds.includes(Number(tweetId));
  const authedId = isAuthed();
  return {
    hearted,
    tweetId,
    authedId
  };
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleHeart, handleUnheart }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HeartTweetBtn);
