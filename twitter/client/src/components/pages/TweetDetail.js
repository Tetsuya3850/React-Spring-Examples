import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleGetTweet } from "../../reducers/detailReducer";
import TweetPreview from "../parts/TweetPreview";
import UserPreview from "../parts/UserPreview";

class TweetDetail extends React.Component {
  componentDidMount() {
    const { match, handleGetTweet } = this.props;
    handleGetTweet(match.params.tweetId);
  }

  render() {
    const { isFetching, error, heartedUsersById, match } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        <TweetPreview tweetId={match.params.tweetId} />
        <div>Hearted Users</div>
        {heartedUsersById.map(userId => (
          <UserPreview key={userId} userId={userId} />
        ))}
      </div>
    );
  }
}

const styles = {
  error: {
    color: "red"
  }
};

const mapStateToProps = ({ detail }) => detail;

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleGetTweet }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TweetDetail);
