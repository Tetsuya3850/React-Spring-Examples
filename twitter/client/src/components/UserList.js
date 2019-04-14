import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { handleFetchUserList } from "../reducers/userListReducer";
import UserPreview from "./UserPreview";

class UserList extends React.Component {
  componentDidMount() {
    this.props.handleFetchUserList();
  }

  render() {
    const { isFetching, error, userListByIds } = this.props;

    if (isFetching) {
      return <div>LOADING...</div>;
    }

    if (!isFetching && error) {
      return <div style={styles.error}>{error}</div>;
    }

    return (
      <div>
        {userListByIds.map(userId => (
          <UserPreview key={userId} userId={userId} />
        ))}
      </div>
    );
  }
}

const styles = {
  error: {
    color: "red"
  }
};

const mapStateToProps = ({ userList }) => userList;

const mapDispatchToProps = dispatch =>
  bindActionCreators({ handleFetchUserList }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserList);
