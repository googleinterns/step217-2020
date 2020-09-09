import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import YouTube from "react-youtube";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import CircularProgress from '@material-ui/core/CircularProgress';
import objectEquals from "../helpers/objectEquals";
import axios from "axios";

const styles = (theme) => ({
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

  /**
   * Load component if entity component still loading.
   * Show error if entity component rendered with error.
   * Find videos based on the most salient word if 
   * entities were found.
   * @param {Object} entityState 
   */
  getVideos = (entityState) => {
    if (entityState == undefined || entityState.isLoading) {
      this.setState({ isLoading: true });
    } else if (entityState.errorMsg) {
      this.setState({
        error: new Error(entityState.errorMsg),
        isLoading: false,
      });
    } else if (entityState.entityAnalysisInfo.length == 0) {
      this.setState({
        error: new Error("No YouTube videos were found, because no entities were found"),
        isLoading: false,
      });
    } else {
      axios
        .get(
          `/youtube?query=${entityState.entityAnalysisInfo[0].name}`
        )
        .then((result) => result.data)
        .then((videoIds) => {
          this.setState({
            videoIds: videoIds,
            isLoading: false,
          });
        })
        .catch((error) =>
          this.setState({
            error,
            isLoading: false,
          })
        );
    }
  };

  componentDidUpdate(prevProps, prevState) {
    /* If state has changed, send it to songInfo component */
    if (!objectEquals(prevState, this.state)) {
      this.props.onChangeState(this.state, () => {});
    }
    /**
     * If entityState has changed and videoIds wasn't loaded before,
     * change the component state based on new entityState
     */
    if (!this.props.sendedInfo.wasSended
        && !objectEquals(prevProps.entityState, this.props.entityState)) {
      this.getVideos(this.props.entityState);
    }
  }

  /** 
   * Get youtube video ids if they were sended before
   * or load it instead.
   */
  componentDidMount() {
    if (this.props.sendedInfo.wasSended) {
      this.setState({ videoIds: this.props.sendedInfo.info });
    } else {
      this.getVideos(this.props.entityState);
    }
  }

  render() {
    const classes = this.props.classes;

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

    return (
      <div>
        <List className={classes.youTubeRecommendationsList}>
          {this.state.videoIds.map((videoId, index) => (
            <ListItem key={index + 1} className={classes.youTubeRecommendationsListItem}>
              <YouTube
                videoId={videoId}
              />
            </ListItem>
          ))}
        </List>
      </div>
    );
  }
}

YouTubeRecommendations.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(YouTubeRecommendations);