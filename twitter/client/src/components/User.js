import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { Link } from "react-router-dom";
import { handleFetchUserFeed } from "../reducers/userFeedReducer";
import TweetPreview from "./TweetPreview";
import FollowBtn from "./FollowBtn";

class User extends React.Component {
  componentDidMount() {
    const { match, handleFetchUserFeed } = this.props;
    handleFetchUserFeed(match.params.userId);
  }

  render() {
    const { isFetching, error, user, userTweetIds, followed, me } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        <div style={styles.userInfo}>
          <div>Id: {user.username}</div>
          <Link to={`/users/following/${user.id}`}>
            Following: {user.followingCount}
          </Link>
          <Link to={`/users/followers/${user.id}`}>
            Followers: {user.followersCount}
          </Link>
          <Link to={`/users/hearts/${user.id}`}>Hearted Tweets</Link>
          <FollowBtn me={me} followed={followed} user={user} />
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
    display: "flex",
    flexDirection: "column",
    borderBottom: "1px solid grey"
  },
  error: {
    color: "red"
  }
};

const mapStateToProps = ({ users, userFeed, ownInfo }, { match, authedId }) => {
  const user = users[match.params.userId] ? users[match.params.userId] : {};
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
    user,
    userTweetIds,
    followed,
    me
  };
};

const mapDispatchToProps = dispatch => {
  return bindActionCreators({ handleFetchUserFeed }, dispatch);
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(User);
