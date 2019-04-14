import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import * as api from "../api";
import UserPreview from "./UserPreview";
import { addUsers } from "../reducers/usersReducer";
import { user } from "../reducers/schema";
import { normalize } from "normalizr";

class Following extends React.Component {
  state = {
    followingByIds: []
  };
  async componentDidMount() {
    const { data } = await api.getFollowing(this.props.match.params.userId);
    const normalizedData = normalize(data, [user]);
    this.setState({ followingByIds: normalizedData.result });
    this.props.addUsers(normalizedData.entities.users);
  }
  render() {
    const { followingByIds } = this.state;
    return (
      <div>
        <div>Following</div>
        {followingByIds.map(userId => (
          <UserPreview key={userId} userId={userId} />
        ))}
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ addUsers }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(Following);
