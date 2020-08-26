import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';
import axios from "axios";

const styles = (theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
  },
  sentimentVariableInfo: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
  },
  sentimentVariableInfoIcon: {
    paddingLeft: "15px",
  }
});

/**
 * Displays search component for song lyrics.
 */
class SentimentAnalysisInfo extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      sentimentAnalysisInfo: {},
      isLoading: false,
      error: null,
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .post("/sentiment", {}, {
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        params: {
          lyrics: this.props.lyrics  
        }
      })
      .then((result) => result.data)
      .then((sentimentAnalysisInfo) =>
        this.setState({
          sentimentAnalysisInfo: sentimentAnalysisInfo,
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
    const classes = this.props.classes;

    if (this.state.error) {
      return (
        <div>
          <p>{this.state.error.message}</p>
        </div>
      );
    }

    if (this.state.isLoading) {
      return (
        <div>
          <CircularProgress style={{ color: "black" }} />;
        </div>
      );
    }

    return (
      <div>
        <Typography variant="h4">Sentiment Analysis</Typography>
        <div className={classes.sentimentVariableInfo}>
          <p>Score: {this.state.sentimentAnalysisInfo.score}</p>
          <Tooltip
            className={classes.sentimentVariableInfoIcon} 
            title="Shows the overall positivity of the text, ranging from -1.0 to 1.0." 
            placement="right">
            <InfoIcon/>
          </Tooltip>
        </div>
        <div className={classes.sentimentVariableInfo}>
          <p>Magnitude: {this.state.sentimentAnalysisInfo.magnitude}</p>
          <Tooltip 
            className={classes.sentimentVariableInfoIcon} 
            title="Represents how strong the sentiment is, ranging from 0.0 to +inf." 
            placement="right">
            <InfoIcon/>
          </Tooltip>
        </div>
      </div>
    );
  }
}

SentimentAnalysisInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SentimentAnalysisInfo);