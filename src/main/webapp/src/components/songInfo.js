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
import axios from "axios";

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

    this.saveState = this.saveState.bind(this);
    this.isReady = this.isReady.bind(this);
    this.sendSongInfo = this.sendSongInfo.bind(this);

    if (props.location.state) {
      this.state = {
        wasSended: false,
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

  saveState() {
    const json = JSON.stringify(this.state);
    localStorage.setItem("state", json);
  }

  isReady(componentState) {
      return (componentState && componentState.isLoading === false && componentState.errorMsg === null);
  }

  sendSongInfo(state) {
    if (!state.wasSended && this.isReady(state.youTubeState) && this.isReady(state.entityState) && this.isReady(state.sentimentState)) {
      axios
        .post("/song-info", {
          parentSong: {
            artist: state.artistName,
            name: state.songName,
          },
          lyrics: state.lyrics,
          score: state.sentimentState.sentimentAnalysisInfo.score,
          magnitude: state.sentimentState.sentimentAnalysisInfo.magnitude,
          topSalientEntities: state.entityState.entityAnalysisInfo,
          youTubeIds: state.youTubeState.videoIds,
        })
        .then(() => {
            this.setState({ wasSended: true });
        })
        .catch((error) =>
          console.log(error.message)
        );
      }
  }

  componentDidUpdate(prevProps, prevState) {
    if (!objectEquals(prevState, this.state)) {
      this.saveState();
      this.sendSongInfo(this.state);
    }
  }

  componentDidMount() {
    const json = localStorage.getItem("state");
    try {
      const state = JSON.parse(json);
      this.setState({state}, () => this.sendSongInfo(state));
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

    const sendedInfo = (info) => {
      if (this.state.wasSended) 
        return {
          wasSended: true,
          info: info,
        }
      else {
        return {
          wasSended: false,
        }
      }
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
              <SentimentAnalysisInfo
                sendedInfo={sendedInfo(this.state.sentimentState ? this.state.sentimentState.sentimentAnalysisInfo : undefined)}
                lyrics={this.state.lyrics} 
                onChangeState={this.handleSentimentChange} />
            </div>
            <div class="song-entity-analysis">
              <Typography variant="h4">Entity Analysis</Typography>
              <EntityAnalysisInfo 
                sendedInfo={sendedInfo(this.state.entityState ? this.state.entityState.entityAnalysisInfo : undefined)} 
                lyrics={this.state.lyrics} 
                onChangeState={this.handleEntityChange} />
            </div>
          </div>
        </div>
        <div className={classes.youTubeRecommendationsSection}>
          <Typography variant="h4">YouTube Recommendations</Typography>
          <YouTubeRecommendations 
            sendedInfo={sendedInfo(this.state.youTubeState ? this.state.youTubeState.videoIds : undefined)} 
            entityState={this.state.entityState} 
            onChangeState={this.handleYouTubeChange} />
        </div>
      </div>
    );
  }
}

SongInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SongInfo);