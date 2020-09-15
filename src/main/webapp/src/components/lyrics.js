import React from "react";
import Typography from "@material-ui/core/Typography";
import axios from "axios";

/**
 * Displays search component for song lyrics.
 */
class Lyrics extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      speech: [],
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
      .then((speech) =>
        this.setState({
          speech: speech,
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

    return (
      <div>
        <Typography variant="h4">Lyrics</Typography>
        <p style={{ whiteSpace: "pre-wrap" }}>{lyrics}</p>
      </div>
    );
  }
}

export default Lyrics;
