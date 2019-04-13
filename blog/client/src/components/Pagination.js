import React from "react";
import { Link } from "react-router-dom";

const Pagination = ({ first, last, page }) => (
  <div style={styles.container}>
    {!first && <Link to={`/?page=${page - 1}`}>Prev</Link>}
    {!last && <Link to={`/?page=${page + 1}`}>Next</Link>}
  </div>
);

const styles = {
  container: {
    display: "flex",
    justifyContent: "space-around"
  }
};

export default Pagination;
