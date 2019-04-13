import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import queryString from "query-string";
import { handleFetchFeed } from "../reducers/feedReducer";
import ArticlePreview from "./ArticlePreview";
import Pagination from "./Pagination";

class Feed extends React.Component {
  componentDidMount() {
    this.fetchPageFeed();
  }

  componentDidUpdate(prevProps) {
    if (this.props.location.search !== prevProps.location.search) {
      this.fetchPageFeed();
    }
  }

  fetchPageFeed = () => {
    const values = queryString.parse(this.props.location.search);
    const page = values.page ? values.page : 0;
    this.props.handleFetchFeed(page, lastPage => {
      this.props.history.push(`/?page=${lastPage}`);
    });
  };

  render() {
    const { isFetching, error, feedByIds, first, last } = this.props;
    const values = queryString.parse(this.props.location.search);
    const page = values.page ? values.page : 0;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        {feedByIds.map(articleId => (
          <ArticlePreview key={articleId} articleId={articleId} />
        ))}
        <Pagination first={first} last={last} page={Number(page)} />
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
