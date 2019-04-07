import React, { Component } from "react";
import * as api from "./api";

class UploadForm extends Component {
  state = {
    localimg: null,
    error: ""
  };

  handleFormSubmit = async e => {
    e.preventDefault();
    try {
      var fd = new FormData();
      fd.append("file", this.state.localimg);
      await api.upload(fd);
      this.props.history.push("/");
    } catch (error) {
      console.log(error);
      this.setState({ error: "Something went Wrong!" });
    }
  };

  render() {
    return (
      <div>
        <form onSubmit={this.handleFormSubmit}>
          <label>Max file size is 1MB</label>

          <input
            type="file"
            name="pic"
            accept="image/*"
            required
            onChange={event => {
              var file = event.target.files[0];
              this.setState({ localimg: file });
            }}
          />

          <br />
          <input type="submit" value="Upload" />
          <p style={styles.error}>{this.state.error}</p>
        </form>
      </div>
    );
  }
}

const styles = {
  error: {
    color: "red"
  }
};

export default UploadForm;
