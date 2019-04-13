import React from "react";
import * as api from "../api";

class EditArticle extends React.Component {
  state = {
    title: "",
    text: ""
  };

  async componentDidMount() {
    const { data } = await api.getArticle(this.props.match.params.articleId);
    const { title, text } = data;
    this.setState({ title, text });
  }

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
    const { title, text } = this.state;
    const { articleId } = this.props.match.params;
    if (title && text) {
      const payload = { title, text };
      try {
        await api.editArticle(articleId, payload);
        this.props.history.push(`/articles/details/${articleId}`);
      } catch (error) {
        console.log(error);
      }
    }
  };

  render() {
    const { title, text } = this.state;

    return (
      <form onSubmit={this.handleFormSubmit}>
        <h3>Edit Article Form</h3>

        <div>
          <div>
            <label>Title</label>
            <span> * </span>
          </div>

          <input
            name="title"
            type="text"
            value={title}
            onChange={this.handleInputChange}
            required
          />
        </div>

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
            rows="15"
            style={styles.textarea}
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

export default EditArticle;
