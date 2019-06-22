import React from "react";
import * as api from "../../api";

class NewTweet extends React.Component {
  state = {
    text: ""
  };

  handleInputChange = event => {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;
    this.setState({
      [name]: value
    });
  };

  handleFormSubmit = async event => {
    event.preventDefault();
    const { text } = this.state;
    if (text) {
      const payload = { text };
      try {
        await api.postTweet(payload);
        this.props.history.push("/");
      } catch (error) {
        console.log(error);
      }
    }
  };

  render() {
    const { text } = this.state;

    return (
      <form onSubmit={this.handleFormSubmit}>
        <h3>New Tweet Form</h3>

        <div>
          <div>
            <label>Text</label>
            <span> * </span>
          </div>

          <textarea
            name="text"
            type="text"
            value={text}
            onChange={this.handleInputChange}
            required
            rows="8"
            style={styles.textarea}
            maxLength={140}
          />
        </div>

        <input type="submit" value="Submit!" />
      </form>
    );
  }
}

const styles = {
  textarea: {
    width: "100%"
  }
};

export default NewTweet;
