import React from "react";
import PropTypes from "prop-types";
import { createMuiTheme, withStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';
import Tooltip from '@material-ui/core/Tooltip';
import InfoIcon from '@material-ui/icons/Info';
import objectEquals from '../helpers/objectEquals';
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
  },
});

const LargeTextTooltip = withStyles({
  tooltip: {
    fontSize: "14px",
  }
})(Tooltip);

/**
 * Displays sentiment analysis info for the lyrics.
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

  /* If state has changed, send it to songInfo component */
  componentDidUpdate(prevProps, prevState) {
    if (!objectEquals(prevState, this.state)) {
      this.props.onChangeState(this.state);
    }
  }

  /** 
   * Get sentiment analysis info if it was sent before
   * or load it instead.
   */
  componentDidMount() {
    if (this.props.sentInfo.wasSent) {
      this.setState({ sentimentAnalysisInfo: this.props.sentInfo.info });
    } else {
      this.setState({ isLoading: true });

      axios
        .post("/sentiment", {
          lyrics: this.props.lyrics
        })
        .then((result) => result.data)
        .then((sentimentAnalysisInfo) =>
          this.setState({
            sentimentAnalysisInfo: sentimentAnalysisInfo,
            isLoading: false,
            error: null,
          })
        )
        .catch((error) =>
          this.setState({
            error,
            isLoading: false,
          })
        );
    }
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
          <CircularProgress style={{ color: "black" }} />
        </div>
      );
    }

    return (
      <div>
        <div className={classes.sentimentVariableInfo}>
          <p>Score: {this.state.sentimentAnalysisInfo.score}</p>
          <LargeTextTooltip
            className={classes.sentimentVariableInfoIcon} 
            title="Shows the overall positivity of the text, ranging from -1.0 to 1.0." 
            placement="right">
            <InfoIcon fontSize="inherit" />
          </LargeTextTooltip>
        </div>
        <div className={classes.sentimentVariableInfo}>
          <p>Magnitude: {this.state.sentimentAnalysisInfo.magnitude}</p>
          <LargeTextTooltip
            className={classes.sentimentVariableInfoIcon} 
            title="Represents how strong the sentiment is, ranging from 0.0 to +inf." 
            placement="right">
            <InfoIcon fontSize="inherit" />
          </LargeTextTooltip>
        </div>
      </div>
    );
  }
}

SentimentAnalysisInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SentimentAnalysisInfo);
