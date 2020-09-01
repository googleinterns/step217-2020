import React, { Component } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { withStyles } from "@material-ui/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Button from "@material-ui/core/Button";

const styles = (theme) => ({
  root: {
    flexGrow: 1
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  navBar: {
    display: "flex",
    justifyContent: "space-between"
  },
  title: {
    fontFamily: "ABeeZee",
    fontSize: "20px",
    textTransform: "none",
    fontWeight: 500
  }
});

class Navbar extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const classes = this.props.classes;
    const loggedInInfo = this.props.loggedInInfo;
    return (
      <div className={classes.root}>
        <AppBar style={{ background: "#733F94" }} position="static">
          <Toolbar className={classes.navBar}>
            <Button
              className={classes.title}
              color="inherit"
              to="/"
              component={Link}
            >
              Alpollo
            </Button>
            <div>
              <Button color="inherit" to="/search" component={Link}>
                Search
              </Button>
              <Button color="inherit" href={loggedInInfo.authUrl}>
                {loggedInInfo.isLoggedIn ? "Logout" : "Login"}
              </Button>
            </div>
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

Navbar.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Navbar);
