import React from "react";
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';
import ReactAudioPlayer from 'react-audio-player';
import axios from "axios";

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
        <Typography variant="h4">Lyrics</Typography>
        <ReactAudioPlayer
          src={this.state.speechUrl}
          autoPlay
          controls
        />
        <p style={{ whiteSpace: "pre-wrap" }}>{lyrics}</p>
      </div>
    );
  }
}

export default Lyrics;
