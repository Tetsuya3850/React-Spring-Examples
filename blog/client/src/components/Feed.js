import React, { Component } from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import queryString from "query-string";
import { handleFetchFeed } from "../reducers/feedReducer";
import ArticlePreview from "./ArticlePreview";

class Feed extends Component {
  componentDidMount() {
    const values = queryString.parse(this.props.location.search);
    const page = values.page ? values.page : 0;
    this.props.handleFetchFeed(page);
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
        <div>
          {feedByIds.map(articleId => (
            <ArticlePreview key={articleId} articleId={articleId} />
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

const mapStateToProps = ({ feed }) => feed;

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFetchFeed }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Feed);
