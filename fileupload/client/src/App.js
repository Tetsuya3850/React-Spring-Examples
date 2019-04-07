import React, { Component } from "react";
import * as api from "./api";

class App extends Component {
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

  handleFormSubmit = async e => {
    e.preventDefault();
    try {
      var fd = new FormData();
      fd.append("file", this.state.localimg);
      var response = await api.upload(fd);
    } catch (error) {
      console.log(error);
    }
  };

  render() {
    return (
      <div>
        <form onSubmit={this.handleFormSubmit}>
          <input
            type="file"
            name="pic"
            accept="image/*"
            onChange={event => {
              var file = event.target.files[0];
              this.setState({ localimg: file });
            }}
          />
          <br />
          <input type="submit" value="Upload" />
        </form>

        {this.state.posts.map(({ id, filename, fileurl }) => (
          <div key={id}>
            <p>{filename}</p>
            <img src={fileurl} alt={filename} />
          </div>
        ))}
      </div>
    );
  }
}

export default App;
