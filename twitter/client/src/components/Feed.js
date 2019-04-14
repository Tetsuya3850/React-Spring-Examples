import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleFetchFeed } from "../reducers/feedReducer";
import TweetPreview from "./TweetPreview";

class Feed extends React.Component {
  componentDidMount() {
    this.props.handleFetchFeed();
  }

  render() {
    const { isFetching, error, feedByIds } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        {feedByIds.map(tweetId => (
          <TweetPreview key={tweetId} tweetId={tweetId} />
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

const mapStateToProps = ({ feed }) => feed;

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFetchFeed }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Feed);
