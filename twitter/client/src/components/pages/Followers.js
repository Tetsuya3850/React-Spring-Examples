import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import * as api from "../../api";
import UserPreview from "../parts/UserPreview";
import { addUsers } from "../../reducers/usersReducer";
import { user } from "../../reducers/schema";
import { normalize } from "normalizr";

class Followers extends React.Component {
  state = {
    followersByIds: []
  };

  async componentDidMount() {
    const { data } = await api.getFollowers(this.props.match.params.userId);
    const normalizedData = normalize(data, [user]);
    this.setState({ followersByIds: normalizedData.result });
    this.props.addUsers(normalizedData.entities.users);
  }

  render() {
    const { followersByIds } = this.state;

    return (
      <div>
        <div>Followers</div>
        {followersByIds.map(userId => (
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
)(Followers);
