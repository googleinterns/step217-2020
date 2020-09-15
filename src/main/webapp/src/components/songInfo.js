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
import CircularProgress from '@material-ui/core/CircularProgress';
import axios from "axios";
import JSONbig from "json-bigint";

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

    this.handleSentimentChange = this.handleSentimentChange.bind(this);
    this.handleEntityChange = this.handleEntityChange.bind(this);
    this.handleYouTubeChange = this.handleYouTubeChange.bind(this);

    this.saveState = this.saveState.bind(this);
    this.isReady = this.isReady.bind(this);
    this.sendSongInfo = this.sendSongInfo.bind(this);
    this.getSongInfo = this.getSongInfo.bind(this);

    /** If we have Song id */
    if (props.match.params.id) {
      this.state = {
        /** We don't send song information in this case. */
        wasSent: true,
        id: props.match.params.id,
        isLoading: true,
        error: null,
      }
      localStorage.removeItem("state");
    } /** If we have basic song information in the location state */ 
    else if (props.location.state) {
      this.state = {
        wasSent: false,
        artistName: props.location.state.artistName,
        songName: props.location.state.songName,
        lyrics: props.location.state.lyrics,
      };
      const json = JSON.stringify(this.state);
      localStorage.setItem("state", json);
    } /** Retrive state from the localStorage if it exists. */ 
    else {
      const json = localStorage.getItem("state");
      try {
        const state = JSON.parse(json);
        this.state = state;
      } catch (_) {
        this.state = undefined;
      }
    }
  }

  /* Save new sentiment component state */
  handleSentimentChange(state, callback) {
    this.setState({
      sentimentState: {
        sentimentAnalysisInfo: state.sentimentAnalysisInfo,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    }, callback);
  }

  /* Save new entity component state */
  handleEntityChange(state, callback) {
    this.setState({
      entityState: {
        entityAnalysisInfo: state.entityAnalysisInfo,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    }, callback);
  }

  /* Save new YouTube component state */
  handleYouTubeChange(state, callback) {
    this.setState({
      youTubeState: {
        videoIds: state.videoIds,
        isLoading: state.isLoading,
        errorMsg: state.error ? state.error.message : null,
      }
    }, callback);
  }

  /** Save state of the component to localStorage. */
  saveState() {
    const json = JSON.stringify(this.state);
    localStorage.setItem("state", json);
  }

  /** Checks that all analysis components are ready and info wasn't sent before.  */
  isReady(state) {
    /** Checks if the state of the component was loaded successfully. */
    const isComponentReady = function(componentState) {
      return (componentState && !componentState.isLoading && !componentState.errorMsg);
    };
    return !state.wasSent && isComponentReady(state.youTubeState) && isComponentReady(state.entityState) && isComponentReady(state.sentimentState);
  }

  /** If all analysis components are ready, send SongInfo object to the server. */
  sendSongInfo(state) {
    if (this.isReady(state)) {
      axios
        .post("/analysis-info", {
          song: {
            artist: state.artistName,
            name: state.songName,
          },
          lyrics: state.lyrics,
          score: state.sentimentState.sentimentAnalysisInfo.score,
          magnitude: state.sentimentState.sentimentAnalysisInfo.magnitude,
          interpretation: state.sentimentState.sentimentAnalysisInfo.interpretation,
          topSalientEntities: state.entityState.entityAnalysisInfo,
          youTubeIds: state.youTubeState.videoIds,
        })
        .then(() => {
            this.setState({ wasSent: true });
        })
        .catch((error) =>
          console.log(error.message)
        );
      }
  }

  /** Get song info by the id */
  getSongInfo(id) {
    this.setState({ isLoading: true });
    
    axios
      .get(`/analysis-info?id=${id}`, {
        headers: {
          'Content-Type': 'application/json'
        },
        transformResponse: data => JSONbig.parse(data),
      })
      .then((result) => result.data)
      .then((songInfo) => {
        this.handleSentimentChange({
          sentimentAnalysisInfo: {
            score: songInfo.score,
            magnitude: songInfo.magnitude,
            interpretation: songInfo.interpretation
          },
          isLoading: false,
          error: null,
        }, () => this.handleEntityChange({
          entityAnalysisInfo: songInfo.topSalientEntities,
          isLoading: false,
          error: null,
        }, () => this.handleYouTubeChange({
          videoIds: songInfo.youTubeIds,
          isLoading: false,
          error: null,
        }, () => this.setState({
          artistName: songInfo.song.artist,
          songName: songInfo.song.name,
          lyrics: songInfo.lyrics,
          isLoading: false,
          error: null,
        }, () => this.saveState()))));
      })
      .catch((error) =>
        this.setState({
          error,
          isLoading: false,
        }, this.saveState())
      );
  }

  /** Save the state and send song info if there's no song id. */
  componentDidUpdate(prevProps, prevState) {
    if (this.state.id === undefined && !objectEquals(prevState, this.state)) {
      this.saveState();
      this.sendSongInfo(this.state);
    }
  }

  componentDidMount() {
    const json = localStorage.getItem("state");
    /** If this song has id and wasn't loaded before. */
    if (json == undefined && this.state.id !== undefined) {
      this.getSongInfo(this.state.id);
    } else {
      try {
        const state = JSON.parse(json);
        this.setState({state}, () => this.sendSongInfo(state));
      } catch (_) {}
    }
  }

  render() {
    const classes = this.props.classes;

    if (this.state == undefined) {
      return <Redirect to="/search" />
    }

    if (this.state.error) {
      console.log(this.state.error.message)
      return (
        <div>
          <p>{`Something went wrong, please try again later.`}</p>
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

    const songInfo = {
      bandName: this.state.artistName.toUpperCase(),
      songName: this.state.songName.toUpperCase(),
      lyrics: this.state.lyrics,
      sentimentAnalysis: this.state.sentimentState ? this.state.sentimentState.sentimentAnalysisInfo : undefined,
      entityAnalysis: this.state.entityState ? this.state.entityState.entityAnalysisInfo : undefined,
      youTubeRecommendations: this.state.youTubeState ? this.state.youTubeState.videoIds : undefined,
    };

    /** 
     * Creates information for the component if it was sent before.
     * Analysis components use it to avoid loading the same data and 
     * do API requests several times.
     */
    const sentInfo = (info) => {
      if (this.state.wasSent) 
        return {
          wasSent: true,
          info: info,
        }
      else {
        return {
          wasSent: false,
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
                sentInfo={sentInfo(this.state.sentimentState ? this.state.sentimentState.sentimentAnalysisInfo : undefined)}
                lyrics={this.state.lyrics} 
                onChangeState={this.handleSentimentChange} />
            </div>
            <div class="song-entity-analysis">
              <Typography variant="h4">Entity Analysis</Typography>
              <EntityAnalysisInfo 
                sentInfo={sentInfo(this.state.entityState ? this.state.entityState.entityAnalysisInfo : undefined)} 
                lyrics={this.state.lyrics} 
                onChangeState={this.handleEntityChange} />
            </div>
          </div>
        </div>
        <div className={classes.youTubeRecommendationsSection}>
          <Typography variant="h4">YouTube Recommendations</Typography>
          <YouTubeRecommendations 
            sentInfo={sentInfo(this.state.youTubeState ? this.state.youTubeState.videoIds : undefined)} 
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