import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { withRouter } from "react-router";
import CircularProgress from '@material-ui/core/CircularProgress';
import axios from "axios";

const styles = () => ({
  root: {
    margin: "50px",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  title: {
    marginBottom: "20px",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-end",
  },
  textField: {
    width: "500px",
    marginBottom: "20px",
  },
  submitButton: {
    background: "#733F94",
    color: "white",
    "&:hover": {
      background: "rgba(115, 63, 148, 0.5)",
    },
  },
});

/**
 * Change TextField outline border default colors.
 */
const StyledTextField = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "black",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: "#733F94",
    },
    "& .MuiOutlinedInput-root": {
      "&.Mui-focused fieldset": {
        borderColor: "#733F94",
      },
    },
  },
})(TextField);

/**
 * Displays search component for song lyrics.
 */
class Search extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      artistName: "",
      songName: "",
      isLoading: false,
      error: null,
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  /**
   * Save printed text to state while printing.
   * @param {Event} event 
   */
  handleChange(event) {
    this.setState({ [event.target.id]: event.target.value });
    this.setState({ error: null, isLoading: false });
  }

  /**
   * Redirect to song page after submitting the search form.
   * @param {Event} event 
   */
  handleSubmit(event) {
    event.preventDefault();
    this.setState({ isLoading: true });

    axios
      .get(
        `https://api.lyrics.ovh/v1/${this.state.artistName}/${this.state.songName}`
      )
      .then((result) => result.data)
      .then((response) => {
        this.setState({
          lyrics: response.lyrics,
          isLoading: false,
        });
        this.props.history.push({
          pathname: '/song',
          state: {
            lyrics: response.lyrics,
            artistName: this.state.artistName,
            songName: this.state.songName,
          },
        });
      })
      .catch((error) =>
        this.setState({
          error:
            error.response.data.error === undefined
              ? error
              : new Error(error.response.data.error),
          isLoading: false,
        })
      );
  }

  render() {
    const classes = this.props.classes;

    return (
      <div className={classes.root}>
        <Typography className={classes.title} variant="h3">
          Find your song!
        </Typography>
        <div>
          <form
            onSubmit={this.handleSubmit}
            className={classes.form}
            noValidate
            autoComplete="off"
          >
            <StyledTextField
              className={classes.textField}
              id="artistName"
              label="Artist"
              variant="outlined"
              value={this.state.artistName}
              onChange={this.handleChange}
            />
            <StyledTextField
              className={classes.textField}
              id="songName"
              label="Song"
              variant="outlined"
              value={this.state.songName}
              onChange={this.handleChange}
            />
            <Button className={classes.submitButton} type="submit">
              Submit
            </Button>
          </form>
        </div>
        {(() => {
          if (this.state.error)
            return (
              <div>
                <p>{this.state.error.message}</p>
              </div>
            );
          if (this.state.isLoading)
            return (
              <div>
                <CircularProgress style={{ color: "black" }} />;
              </div>
            );
        })()}
      </div>
    );
  }
}

Search.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withRouter(withStyles(styles)(Search));
