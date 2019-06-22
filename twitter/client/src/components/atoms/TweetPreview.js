import React from "react";
import { connect } from "react-redux";
import { Link, withRouter } from "react-router-dom";
import moment from "moment";
import HeartTweetBtn from "./HeartTweetBtn";
import DeleteTweetBtn from "./DeleteTweetBtn";
import { isAuthed } from "../../tokenUtils";

const TweetPreview = ({ tweet, user, isOwner, match }) => (
  <div style={styles.container}>
    <Link to={`/users/details/${user.id}`}>{user.username}</Link>
    <div>{moment(tweet.created).format("LLL")}</div>
    <div>{tweet.text}</div>
    <div>
      <HeartTweetBtn tweetId={tweet.id} />
      <span>{tweet.heartCount}</span>
      {match.path === "/tweets/details/:tweetId" ? (
        <DeleteTweetBtn tweetId={tweet.id} isOwner={isOwner} />
      ) : (
        <Link to={`/tweets/details/${tweet.id}`}>--></Link>
      )}
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
  }
};

const mapStateToProps = ({ tweets, users }, { tweetId }) => {
  const tweet = tweets[tweetId] ? tweets[tweetId] : {};
  const user = users[tweet.person] ? users[tweet.person] : {};
  const isOwner = isAuthed() === user.id;
  return {
    tweet,
    user,
    isOwner
  };
};

export default withRouter(
  connect(
    mapStateToProps,
    null
  )(TweetPreview)
);
