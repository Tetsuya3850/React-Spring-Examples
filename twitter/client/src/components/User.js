import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleFetchUserFeed } from "../reducers/userFeedReducer";
import TweetPreview from "./TweetPreview";
import { handleFollow, handleUnfollow } from "../reducers/ownInfoReducer";

class User extends React.Component {
  componentDidMount() {
    const { match, handleFetchUserFeed } = this.props;
    handleFetchUserFeed(match.params.userId);
  }

  render() {
    const {
      isFetching,
      error,
      userInfo,
      userTweetIds,
      followed,
      me,
      handleFollow,
      handleUnfollow
    } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    let followBtn;
    if (!me) {
      followBtn = !followed ? (
        <span onClick={() => handleFollow(userInfo.id)}>Follow</span>
      ) : (
        <span onClick={() => handleUnfollow(userInfo.id)}>Unfollow</span>
      );
    }

    return (
      <div>
        <div style={styles.userInfo}>
          <div>Id: {userInfo.username}</div>
          <div>Following: {userInfo.followingCount}</div>
          <div>Followers: {userInfo.followersCount}</div>
          {followBtn}
        </div>

        <div>
          {userTweetIds.map(tweetId => (
            <TweetPreview key={tweetId} tweetId={tweetId} />
          ))}
        </div>
      </div>
    );
  }
}

const styles = {
  userInfo: {
    borderBottom: "1px solid grey"
  },
  error: {
    color: "red"
  }
};

const mapStateToProps = ({ users, userFeed, ownInfo }, { match, authedId }) => {
  const userInfo = users[match.params.userId] ? users[match.params.userId] : {};
  const userTweetIds = userFeed.userFeedByIds[match.params.userId]
    ? userFeed.userFeedByIds[match.params.userId]
    : [];
  const { isFetching, error } = userFeed;
  const followed = ownInfo.followingUserIds.includes(
    Number(match.params.userId)
  );
  const me = match.params.userId === authedId;
  return {
    isFetching,
    error,
    userInfo,
    userTweetIds,
    followed,
    me
  };
};

const mapDispatchToProps = dispatch => {
  return bindActionCreators(
    { handleFetchUserFeed, handleFollow, handleUnfollow },
    dispatch
  );
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(User);
