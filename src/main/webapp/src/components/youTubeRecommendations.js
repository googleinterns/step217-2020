import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import YouTube from "react-youtube";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import CircularProgress from '@material-ui/core/CircularProgress';
import axios from "axios";

const styles = (theme) => ({
  youTubeVideo: {
    playerVars: {
      autoplay: 1,
    },
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
});

/**
 * Displays sentiment analysis info for the lyrics.
 */
class YouTubeRecommendations extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      videoIds: [],
      isLoading: false,
      error: null,
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .get(`/youtube?query=${this.props.q}`)
      .then((result) => result.data)
      .then((videoIds) => {
        this.setState({
          videoIds: videoIds,
          isLoading: false,
        })
      })
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
          <CircularProgress style={{ color: "black" }} />
        </div>
      );
    }

    return (
      <div>
        <List className={classes.youTubeRecommendationsList}>
          {this.state.videoIds.map((videoId, index) => (
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
    );
  }

  _onReady(event) {
    // Access to player in all event handlers via event.target.
    event.target.pauseVideo();
  }
}

YouTubeRecommendations.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(YouTubeRecommendations);