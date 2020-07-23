import React, { Fragment } from "react";
import logo from "./logo.svg";
import "./App.css";
import Dashboard from "./components/Dashboard";

import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.min.css";
import Header from "./components/Layout/Header";
import AddProject from "./components/Project/AddProject";

import { Provider } from "react-redux"; //Provider - how we define the store for our application
import store from "./store";
import UpdateProject from "./components/Project/UpdateProject";
import ProjectBoard from "./components/ProjectBoard/ProjectBoard";
import AddProjectTask from "./components/ProjectBoard/ProjectTasks/AddProjectTask";
import UpdateProjectTask from "./components/ProjectBoard/ProjectTasks/UpdateProjectTask";
import Landing from "./components/Layout/Landing";
import Register from "./components/UserManagement/Register";
import Login from "./components/UserManagement/Login";
import jwt_decode from "jwt-decode";
import setJwtToken from "./securityUtils/setJwtToken";

import { GET_ERRORS, SET_CURRENT_USER } from "./actions/types";

import { logout } from "./actions/securityActions";

import SecuredRoute from "./securityUtils/SecureRoute";

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setJwtToken(jwtToken);
  const decoded_jwtToken = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decoded_jwtToken,
  });

  const currentTime = Date.now() / 1000;
  if (decoded_jwtToken.exp < currentTime) {
    store.dispatch(logout());

    window.location.href = "/";
  }
}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <Fragment className="App">
          <Header />

          {
            //Public Routes
          }
          <Route exact path="/" component={Landing} />
          <Route exact path="/register" component={Register} />
          <Route exact path="/login" component={Login} />
          {
            //Private routes
          }
          <Switch>
            <SecuredRoute exact path="/dashboard" component={Dashboard} />
            <SecuredRoute exact path="/addProject" component={AddProject} />
            <SecuredRoute
              exact
              path="/updateProject/:id"
              component={UpdateProject}
            />
            <SecuredRoute
              exact
              path="/projectBoard/:id"
              component={ProjectBoard}
            />
            <SecuredRoute
              exact
              path="/addProjectTask/:id"
              component={AddProjectTask}
            />
            <SecuredRoute
              exact
              path="/updateProjectTask/:backlog_id/:pt_id"
              component={UpdateProjectTask}
            />
          </Switch>
        </Fragment>
      </Router>
    </Provider>
  );
}

export default App;
