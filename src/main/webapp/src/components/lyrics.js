import React from "react";
import Typography from "@material-ui/core/Typography";

/**
 * Displays search component for song lyrics.
 */
class Lyrics extends React.Component {
  constructor(props) {
    super(props);
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
