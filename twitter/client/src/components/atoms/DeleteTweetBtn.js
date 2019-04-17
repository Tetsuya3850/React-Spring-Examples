import React from "react";
import { withRouter } from "react-router-dom";
import * as api from "../../api";

const DeleteTweetBtn = ({ tweetId, isOwner, history }) => (
  <span>
    {isOwner ? (
      <button
        onClick={async () => {
          await api.deleteTweet(tweetId);
          history.push(`/`);
        }}
      >
        Delete
      </button>
    ) : (
      <div />
    )}
  </span>
);

export default withRouter(DeleteTweetBtn);
