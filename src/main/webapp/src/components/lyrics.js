import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import AudioPlayer from 'material-ui-audio-player';
import axios from "axios";
import StyledLinearProgress from "../styledComponents/styledLinearProgress";

const styles = () => ({
  root: {
    minWidth: "300px",
    display: "flex",
    flexDirection: "column"
  },
  audio: {
    margin: "20px 0 20px 0",
  }
});

/**
 * Displays search component for song lyrics.
 */
class Lyrics extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      speechUrl: undefined,
      isLoading: false,
      error: null,
    };
  }

  getSpeech = () => {
    this.setState({ isLoading: true });

    axios
      .post("/text-to-speech", {
        lyrics: this.props.lyrics
      })
      .then((result) => result.data)
      .then((json) => {
        const blob = new Blob([new Uint8Array(json)], { type: "audio/mp3" });
        const url = URL.createObjectURL(blob);
        this.setState({
          speechUrl: url,
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

  componentDidMount() {
    this.getSpeech();
  }

  render() {
    const classes = this.props.classes;

    /**
     * Lyrics, returned by lyrics.ovh has `\n\n\n`
     * and `\r\n`.
     * We replace `\n\n\n` with `\n\n`
     * and `\r\n` with `\n` to make it
     * look better.
     */
    const lyrics = this.props.lyrics
      .replace(/\r\n/g, "\n")
      .replace(/\n\n\n/g, "\n\n");
    
    const speechUrl = this.state.speechUrl;

    const AudioPlayerHTML = (
      <AudioPlayer
        src={speechUrl}
      />);

    const LoadingProgressHTML = (
      <div>
        <p>Loading audio file...</p>
        <StyledLinearProgress/>
      </div>
    );

    const AudioErrorHTML = (
      <p>{`Couldn't load the audio, please try again later.`}</p>
    );

    const LyricsHTML = (AudioPlayerStatus) => { return (
      <div className={classes.root}>
        <Typography variant="h4">Lyrics</Typography>
        <div className={classes.audio}>
          {AudioPlayerStatus}
        </div>
        <p style={{ whiteSpace: "pre-wrap" }}>{lyrics}</p>
      </div>);};

    if (this.state.error) {
      console.error(this.state.error)
      return (
        <div>
          {LyricsHTML(AudioErrorHTML)}
        </div>
      );
    }

    if (this.state.isLoading) {
      return (
        <div>
          {LyricsHTML(LoadingProgressHTML)}
        </div>
      );
    }

    return LyricsHTML(AudioPlayerHTML);
  }
}

Lyrics.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Lyrics);
