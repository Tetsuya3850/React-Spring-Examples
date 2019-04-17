import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import UserProfile from "../atoms/UserProfile";
import TweetPreview from "../atoms/TweetPreview";
import { handleFetchUserFeed } from "../../reducers/userFeedReducer";

class User extends React.Component {
  componentDidMount() {
    const { match, handleFetchUserFeed } = this.props;
    handleFetchUserFeed(match.params.userId);
  }

  render() {
    const { isFetching, error, userTweetIds, match } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        <UserProfile userId={match.params.userId} />
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
  error: {
    color: "red"
  }
};

const mapStateToProps = ({ userFeed }, { match }) => {
  const { isFetching, error } = userFeed;
  const userTweetIds = userFeed.userFeedByIds[match.params.userId]
    ? userFeed.userFeedByIds[match.params.userId]
    : [];
  return {
    isFetching,
    error,
    userTweetIds
  };
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFetchUserFeed }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(User);
