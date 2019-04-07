import React, { Component } from "react";
import { Link } from "react-router-dom";
import * as api from "./api";

class Feed extends Component {
  state = {
    posts: []
  };

  async componentDidMount() {
    try {
      const { data } = await api.getPosts();
      this.setState({ posts: data });
    } catch (error) {
      console.log(error);
    }
  }

  render() {
    return (
      <div>
        <Link to="posts/new">Add New Post</Link>
        {this.state.posts.map(({ id, filename, fileurl }) => (
          <div key={id}>
            <p>{filename}</p>
            <img src={fileurl} alt={filename} style={{ width: "100%" }} />
            <hr />
          </div>
        ))}
      </div>
    );
  }
}

export default Feed;
