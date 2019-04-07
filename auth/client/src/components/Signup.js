import React, { Component } from "react";
import * as api from "../api";
import { saveToken } from "../tokenUtils";

class Signup extends Component {
  state = {
    email: "",
    password: "",
    passwordConfirm: "",
    unMatchPwdErr: "",
    signupError: {}
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
    if (this.state.password !== this.state.passwordConfirm) {
      this.setState({ unMatchPwdErr: "Doesn't match!" });
      return;
    }
    const { email, password } = this.state;
    if (email && password) {
      const payload = { username: email, password };
      try {
        await api.signup(payload);
        this.props.history.push("/signin");
      } catch (error) {
        if (error.response) {
          console.log(error);
          console.log(error.response);
          const { data } = error.response;
          this.setState({ signupError: data });
        }
      }
    }
  };

  render() {
    const {
      email,
      password,
      passwordConfirm,
      unMatchPwdErr,
      signupError
    } = this.state;

    return (
      <form onSubmit={this.handleFormSubmit}>
        <h3>Signup Form</h3>

        <div>
          <div>
            <label>Email Address</label>
            <span> * </span>
            <span style={styles.error}>{signupError.email}</span>
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
            pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$"
            minLength="8"
            title="Password must be at least 8 characters and include at least 1 uppercase character, 1 lowercase character, and 1 number."
            required
          />
        </div>

        <div>
          <div>
            <label>Confirm Password</label>
            <span> * </span>
            <span style={styles.error}>{unMatchPwdErr}</span>
          </div>

          <input
            name="passwordConfirm"
            type="password"
            value={passwordConfirm}
            onChange={this.handleInputChange}
            required
          />
        </div>

        <input type="submit" value="Submit!" />
      </form>
    );
  }
}

const styles = {
  error: {
    color: "red"
  }
};

export default Signup;
