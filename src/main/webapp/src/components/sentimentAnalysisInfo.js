import React from "react";
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';
import axios from "axios";

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
      .post("/sentiment", {
        lyrics: this.props.lyrics,
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
        <p>Score: {this.state.sentimentAnalysisInfo.score}</p>
        <p>Magnitude: {this.state.sentimentAnalysisInfo.magnitude}</p>
      </div>
    );
  }
}

export default SentimentAnalysisInfo;
