import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import _ from "lodash";

const ArticlePreview = ({ article, authorInfo }) => (
  <div>
    <div style={styles.container}>
      <Link to={`/articles/details/${article.id}`}>
        {_.truncate(article.title, { length: 12 })}
      </Link>
      <div>
        <span>by </span>
        <Link to={`/users/${article.person}`}>
          {_.truncate(authorInfo.username, { length: 15 })}
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
  const article = articles[articleId] ? articles[articleId] : {};
  const authorInfo = users[article.person] ? users[article.person] : {};
  return {
    article,
    authorInfo
  };
};

export default connect(
  mapStateToProps,
  null
)(ArticlePreview);
