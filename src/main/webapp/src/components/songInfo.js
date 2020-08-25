import React from "react";
import PropTypes from "prop-types";
import YouTube from "react-youtube";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import { Redirect } from "react-router";

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
  youTubeVideo: {
    playerVars: {
      autoplay: 1,
    },
  },
  youTubeRecommendationsSection: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  youTubeRecommendationsList: {
    padding: 0,
    display: "flex",
    flexDirection: "row",
    justifyContent: "center",
    flexWrap: "wrap",
  },
  youTubeRecommendationsListItem: {
    width: "640px",
    margin: "10px",
    listStyleType: "none",
  },
  languageAnalysisSection: {
    display: "flex",
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
      sentimentAnalysis: {
        score: "-0.6",
        magnitude: "1.3",
      },
      entityAnalysis: [
        {
          word: "affair",
          salience: "0.51",
          category: "work of art",
        },
        {
          word: "girl",
          salience: "0.09",
          category: "person",
        },
        {
          word: "film",
          salience: "0.06",
          category: "work of art",
        },
        {
          word: "selling show",
          salience: "0.04",
          category: "work of art",
        },
        {
          word: "God",
          salience: "0.01",
          category: "person",
        },
        {
          word: "Mars",
          salience: "0.01",
          category: "location",
        },
      ],
      youTubeRecommendations: [
        "AZKcl4-tcuo",
        "2zX0lxiM8Xo",
        "kJ095S0MmnA",
        "slIG7Aes7DM",
        "uqeH76RLpO8",
      ],
    };

    // TODO display entities with charts
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
              <p>Score: {songInfo.sentimentAnalysis.score}</p>
              <p>Magnitude: {songInfo.sentimentAnalysis.magnitude}</p>
            </div>
            <div class="song-entity-analysis">
              <Typography variant="h4">Entity Analysis</Typography>
              <List>
                {songInfo.entityAnalysis.map((wordInfo, index) => (
                  <ListItem key={index + 1}>
                    {wordInfo.word +
                      " - " +
                      wordInfo.salience +
                      " - " +
                      wordInfo.category}
                  </ListItem>
                ))}
              </List>
            </div>
          </div>
        </div>
        <div className={classes.youTubeRecommendationsSection}>
          <Typography variant="h4">YouTube Recommendations</Typography>
          <List className={classes.youTubeRecommendationsList}>
            {songInfo.youTubeRecommendations.map((videoId, index) => (
              <ListItem key={index + 1} className={classes.youTubeRecommendationsListItem}>
                <YouTube
                  className={classes.youTubeVideo}
                  videoId={videoId}
                  onReady={this._onReady}
                />
              </ListItem>
            ))}
          </List>
        </div>
      </div>
    );
  }

  _onReady(event) {
    // access to player in all event handlers via event.target
    event.target.pauseVideo();
  }
}

SongInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SongInfo);