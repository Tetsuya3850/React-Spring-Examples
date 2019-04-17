import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { bindActionCreators } from "redux";
import moment from "moment";
import { handleGetArticle } from "../reducers/detailReducer";
import { isAuthed } from "../tokenUtils";
import * as api from "../api";

class ArticleDetail extends React.Component {
  componentDidMount() {
    const { match, handleGetArticle } = this.props;
    handleGetArticle(match.params.articleId);
  }

  render() {
    const { detail, article, authorInfo, isOwner, history } = this.props;
    const { isFetching, error } = detail;
    const { id, title, text, created } = article;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        <div>{title}</div>
        <div>
          {isOwner && (
            <div>
              <Link to={`/articles/edit/${id}`}>Edit</Link>
              <span
                onClick={async () => {
                  await api.deleteArticle(id);
                  history.push(`/`);
                }}
                style={styles.deleteBtn}
              >
                Delete
              </span>
            </div>
          )}
        </div>
        <div>
          <span>by </span>
          <Link to={`/users/${authorInfo.id}`}>{authorInfo.username}</Link>
        </div>
        <div>
          on {moment(created, "yyyymmddhhmmss.sss").format("MMMM Do YYYY")}
        </div>
        <p>{text}</p>
      </div>
    );
  }
}

const styles = {
  error: {
    color: "red"
  },
  deleteBtn: {
    cursor: "pointer"
  }
};

const mapStateToProps = ({ articles, users, detail }, { match }) => {
  const article = articles[match.params.articleId]
    ? articles[match.params.articleId]
    : {};
  const authorInfo = users[article.applicationUser]
    ? users[article.applicationUser]
    : {};
  const isOwner = isAuthed() === article.applicationUser;
  return {
    detail,
    article,
    authorInfo,
    isOwner
  };
};

const mapDispatchToProps = dispatch => {
  return bindActionCreators({ handleGetArticle }, dispatch);
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArticleDetail);
