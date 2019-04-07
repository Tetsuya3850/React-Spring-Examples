import React, { Component } from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Feed from "./Feed";
import UploadForm from "./UploadForm";

class App extends Component {
  render() {
    return (
      <Router>
        <div style={styles.container}>
          <Route exact path="/" component={Feed} />
          <Route path="/posts/new" component={UploadForm} />
        </div>
      </Router>
    );
  }
}

const styles = {
  container: {
    width: 320,
    position: "relative",
    margin: "0 auto"
  }
};

export default App;
