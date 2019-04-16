import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import * as api from "../../api";
import TweetPreview from "../parts/TweetPreview";
import { addUsers } from "../../reducers/usersReducer";
import { addTweets } from "../../reducers/tweetsReducer";
import { tweet } from "../../reducers/schema";
import { normalize } from "normalizr";

class HeartedTweets extends React.Component {
  state = {
    heartedTweetsByIds: []
  };

  async componentDidMount() {
    const { match, addTweets, addUsers } = this.props;
    const { data } = await api.getHeartedTweets(match.params.userId);
    const normalizedData = normalize(data, [tweet]);
    this.setState({ heartedTweetsByIds: normalizedData.result });
    addTweets(normalizedData.entities.tweets);
    addUsers(normalizedData.entities.users);
  }

  render() {
    const { heartedTweetsByIds } = this.state;

    return (
      <div>
        <div>Hearted Tweets</div>
        {heartedTweetsByIds.map(tweetId => (
          <TweetPreview key={tweetId} tweetId={tweetId} />
        ))}
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ addTweets, addUsers }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(HeartedTweets);
