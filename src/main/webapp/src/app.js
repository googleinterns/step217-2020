import React, { Component } from "react";
import Navbar from "./components/navbar";
import RatingList from "./components/ratingList";
import SongInfo from "./components/songInfo";
import axios from "axios";
import {
  HashRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import "./app.css";

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      loggedInInfo: {},
      isLoading: false,
      error: null,
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .get("/auth")
      .then((result) => result.data)
      .then((loggedInInfo) =>
        this.setState({
          loggedInInfo: loggedInInfo,
          isLoading: false,
        })
      )
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        })
      );
  }

  render() {
    const { loggedInInfo, isLoading, error } = this.state;

    if (error) {
      return <p>{error.message}</p>;
    }

    if (isLoading) {
      return <p>Loading ...</p>;
    }

    return (
      <Router>
        <div className="AppMain">
          <Navbar loggedInInfo={loggedInInfo} />

          <Switch>
            <Route path="/song">
              <SongInfo />
            </Route>
            <Route path="/">
              <RatingList />
            </Route>
          </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
