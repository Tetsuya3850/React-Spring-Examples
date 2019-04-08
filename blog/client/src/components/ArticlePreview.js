import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

const ArticlePreview = ({ article, authorInfo }) => (
  <div>
    <div style={styles.container}>
      <Link to={`/articles/details/${article.id}`}>{article.title}</Link>
      <div>
        <span>by </span>
        <Link to={`/users/${article.applicationUser}`}>
          {authorInfo.username}
        </Link>
      </div>
    </div>
  </div>
);

const styles = {
  container: {
    display: "flex",
    justifyContent: "space-between",
    paddingTop: "4px",
    paddingBottom: "4px",
    borderBottom: "1px solid grey"
  }
};

const mapStateToProps = ({ articles, users }, { articleId }) => {
  const article = articles[articleId];
  const authorInfo = users[article.applicationUser];
  return {
    article,
    authorInfo
  };
};

export default connect(
  mapStateToProps,
  null
)(ArticlePreview);
