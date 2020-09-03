import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import SentimentAnalysisInfo from "./sentimentAnalysisInfo";
import EntityAnalysisInfo from "./entityAnalysisInfo";
import YouTubeRecommendations from "./youTubeRecommendations";
import Lyrics from "./lyrics";
import { Redirect } from "react-router";
import objectEquals from "../helpers/objectEquals";

const styles = (theme) => ({
  root: {
    margin: "50px",
  },
  songName: {
    marginBottom: "20px",
    display: "flex",
    alignItems: "center",
    flexDirection: "column",
  },
  songLyrics: {
    paddingRight: "100px",
  },
  languageAnalysisSection: {
    display: "flex",
  },
  youTubeRecommendationsSection: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
});

/**
 * Displays information about song.
 */
class SongInfo extends React.Component {
  constructor(props) {
    super(props);

    const json = localStorage.getItem("state");
    const state = JSON.parse(json);

    this.handleSentimentChange = this.handleSentimentChange.bind(this);
    this.handleEntityChange = this.handleEntityChange.bind(this);
    this.handleYouTubeChange = this.handleYouTubeChange.bind(this);

    if (props.location.state) {
      this.state = {
        artistName: props.location.state.artistName,
        songName: props.location.state.songName,
        lyrics: props.location.state.lyrics,
      };
      const json = JSON.stringify(this.state);
      localStorage.setItem("state", json);
    } else if (state) {
      this.state = state;
    } else {
      this.state = undefined;
    }
  }

  /* Save new sentiment component state */
  handleSentimentChange(state) {
    this.setState({
      sentimentState: {
        sentimentAnalysisInfo: state.sentimentAnalysisInfo,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    });
  }

  /* Save new entity component state */
  handleEntityChange(state) {
    this.setState({
      entityState: {
        entityAnalysisInfo: state.entityAnalysisInfo,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    });
  }

  /* Save new YouTube component state */
  handleYouTubeChange(state) {
    this.setState({
      youTubeState: {
        videoIds: state.videoIds,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    });
  }

  componentDidUpdate(prevProps, prevState) {
    if (!objectEquals(prevState.entityState, this.state)) {
      const json = JSON.stringify(this.state);
      localStorage.setItem("state", json);
    }
  }

  componentDidMount() {
    const json = localStorage.getItem("state");
    try {
      const state = JSON.parse(json);
      this.setState(() => state);
    } catch (_) {}
  }

  render() {
    const classes = this.props.classes;

    if (this.state == undefined) {
      return <Redirect to="/search" />
    }

    // TODO Fetch song information from database.
    const songInfo = {
      bandName: this.state.artistName.toUpperCase(),
      songName: this.state.songName.toUpperCase(),
      lyrics: this.state.lyrics,
      sentimentAnalysis: this.state.sentimentState ? this.state.sentimentState.sentimentAnalysisInfo : undefined,
      entityAnalysis: this.state.entityState ? this.state.entityState.entityAnalysisInfo : undefined,
      youTubeRecommendations: this.state.youTubeState ? this.state.youTubeState.videoIds : undefined,
    };

    return (
      <div className={classes.root}>
        <Typography variant="h3" className={classes.songName}>
          "{songInfo.songName}"
        </Typography>
        <div className={classes.languageAnalysisSection}>
          <div className={classes.songLyrics}>
            <Lyrics lyrics={this.state.lyrics} />
          </div>
          <div>
            <div class="song-sentiment-analysis">
              <Typography variant="h4">Sentiment Analysis</Typography>
              <SentimentAnalysisInfo lyrics={this.state.lyrics} onChangeState={this.handleSentimentChange} />
            </div>
            <div class="song-entity-analysis">
              <Typography variant="h4">Entity Analysis</Typography>
              <EntityAnalysisInfo lyrics={this.state.lyrics} onChangeState={this.handleEntityChange} />
            </div>
          </div>
        </div>
        <div className={classes.youTubeRecommendationsSection}>
          <Typography variant="h4">YouTube Recommendations</Typography>
          <YouTubeRecommendations entityState={this.state.entityState} onChangeState={this.handleYouTubeChange}/>
        </div>
      </div>
    );
  }
}

SongInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SongInfo);