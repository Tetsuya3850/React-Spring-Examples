import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { bindActionCreators } from "redux";
import moment from "moment";
import { handleGetTweet } from "../reducers/detailReducer";
import { isAuthed } from "../tokenUtils";
import * as api from "../api";
import UserPreview from "./UserPreview";

class TweetDetail extends React.Component {
  componentDidMount() {
    const { match, handleGetTweet } = this.props;
    handleGetTweet(match.params.tweetId);
  }

  render() {
    const { detail, tweet, user, history } = this.props;
    const { isFetching, error, heartedUsersById } = detail;
    const { id, text, created } = tweet;
    const isOwner = isAuthed() == tweet.applicationUser;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div style={styles.container}>
        <Link to={`/users/${user.id}`}>{user.username}</Link>
        <div>{moment(created, "yyyymmddhhmmss.sss").toISOString()}</div>
        <span>Number of hearts: {tweet.heartCount}</span>
        {isOwner && (
          <div
            onClick={async () => {
              await api.deleteTweet(id);
              history.push(`/`);
            }}
            style={styles.btn}
          >
            Delete
          </div>
        )}
        <div>{text}</div>
        <hr />
        <div>Hearted Users</div>
        {heartedUsersById.map(userId => (
          <UserPreview key={userId} userId={userId} />
        ))}
      </div>
    );
  }
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    wordWrap: "break-word"
  },
  error: {
    color: "red"
  },
  btn: {
    cursor: "pointer"
  }
};

const mapStateToProps = ({ tweets, users, detail }, { match }) => {
  const tweet = tweets[match.params.tweetId]
    ? tweets[match.params.tweetId]
    : {};
  const user = users[tweet.applicationUser] ? users[tweet.applicationUser] : {};
  return {
    detail,
    tweet,
    user
  };
};

const mapDispatchToProps = dispatch => {
  return bindActionCreators({ handleGetTweet }, dispatch);
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TweetDetail);
