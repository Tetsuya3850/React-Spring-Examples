import React, { Component } from "react";
import axios from "axios";
import * as api from "../api";
import { saveToken } from "../tokenUtils";

class Signin extends Component {
  state = {
    email: "",
    password: "",
    signinError: ""
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
    const { email, password } = this.state;
    if (email && password) {
      try {
        const payload = { username: email, password };
        const { data } = await api.signin(payload);
        saveToken(data);
        axios.defaults.headers.common["authorization"] = `Bearer ${data}`;
        const { from } = this.props.location.state || {
          from: { pathname: "/" }
        };
        this.props.history.push(from.pathname);
      } catch (error) {
        this.setState({ signinError: "Something Went Wrong!" });
      }
    }
  };

  render() {
    const { email, password, signinError } = this.state;

    return (
      <form onSubmit={this.handleFormSubmit}>
        <h3>Signin Form</h3>

        <div>
          <div>
            <label>Email Address</label>
            <span> * </span>
          </div>

          <input
            name="email"
            type="email"
            value={email}
            onChange={this.handleInputChange}
            required
          />
        </div>

        <div>
          <div>
            <label>Password</label>
            <span> * </span>
          </div>

          <input
            name="password"
            type="password"
            value={password}
            onChange={this.handleInputChange}
            required
          />
        </div>

        <input type="submit" value="Submit!" />
        <p style={styles.error}>{signinError}</p>
      </form>
    );
  }
}

const styles = {
  error: {
    color: "red"
  }
};

export default Signin;
