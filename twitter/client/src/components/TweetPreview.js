import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { Link } from "react-router-dom";
import { handleHeart, handleUnheart } from "../reducers/ownInfoReducer";

const TweetPreview = ({ tweet, user, hearted, handleHeart, handleUnheart }) => (
  <div>
    <div style={styles.container}>
      <Link to={`/users/details/${user.id}`}>{user.username}</Link>
      <div>{tweet.text}</div>
      <div>
        {!hearted ? (
          <i
            className="far fa-heart"
            style={styles.btn}
            onClick={() => handleHeart(tweet.id)}
          />
        ) : (
          <i
            className="fas fa-heart"
            style={styles.btn}
            onClick={() => handleUnheart(tweet.id)}
          />
        )}
        <span>{tweet.heartCount}</span>
        <Link to={`/tweets/details/${tweet.id}`}>-></Link>
      </div>
    </div>
  </div>
);

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    paddingTop: "4px",
    paddingBottom: "4px",
    borderBottom: "1px solid grey",
    wordWrap: "break-word"
  },
  btn: {
    cursor: "pointer"
  }
};

const mapStateToProps = ({ tweets, users, ownInfo }, { tweetId }) => {
  const tweet = tweets[tweetId] ? tweets[tweetId] : {};
  const user = users[tweet.applicationUser] ? users[tweet.applicationUser] : {};
  const hearted = ownInfo.heartedTweetIds.includes(tweetId);
  return {
    tweet,
    user,
    hearted
  };
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleHeart, handleUnheart }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TweetPreview);
