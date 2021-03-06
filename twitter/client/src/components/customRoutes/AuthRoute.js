import React from "react";
import { Route } from "react-router-dom";
import { isAuthed } from "../../tokenUtils";

const AuthRoute = ({
  authComponent: AuthComponent,
  unAuthComponent: UnAuthComponent,
  ...rest
}) => (
  <Route
    {...rest}
    render={props => {
      const authedId = isAuthed();
      return authedId ? (
        <AuthComponent {...props} authedId={authedId} />
      ) : (
        <UnAuthComponent {...props} />
      );
    }}
  />
);

export default AuthRoute;
