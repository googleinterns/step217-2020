import React, { Component } from "react";
import PropTypes from "prop-types";
import { withStyles } from '@material-ui/styles';
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";

const styles = (theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
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
          <Toolbar>
            <Typography variant="h6" className={classes.title}>
              Alpollo
            </Typography>
            <Button color="inherit">Search</Button>
            <Button color="inherit" href={loggedInInfo.authUrl}>
              {loggedInInfo.isLoggedIn ? "Logout" : "Login"}
            </Button>
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
