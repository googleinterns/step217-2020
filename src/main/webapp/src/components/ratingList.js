import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import { ListItemIcon } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

const styles = (theme) => ({
  root: {
    marginTop: "50px",
    width: "100%",
    display: "flex",
    alignItems: "center",
    flexDirection: "column",
  },
  listIcon: {
    fontFamily: "ABeeZee",
    fontSize: "28px",
    color: "#3A3838",
  },
});

function EnumeratedList(props) {
  const listItems = props.listItems;
  const classes = props.styleClasses;
  const reactListItems = listItems.map((item, index) => (
    <ListItem key={index + 1}>
      <ListItemIcon className={classes.listIcon}>
        {index + 1}
      </ListItemIcon>
      <ListItemText
        primary={item.songName}
        secondary={item.bandName + '. From album "' + item.albumName + '"'}
      />
    </ListItem>
  ));
  return <List>{reactListItems}</List>;
}

class RatingList extends React.Component {
  render() {
    const classes = this.props.classes;

    // TODO Fetch top songs from database.
    const topSongs = [
      {
        songName: "Yesterday",
        albumName: "Help!",
        bandName: "The Beatles",
      },
      {
        songName: "Bohemian Rhapsody",
        albumName: "A Night at the Opera",
        bandName: "Queen",
      },
      {
        songName: "Hotel California",
        albumName: "Hotel California",
        bandName: "Eagles",
      },
      {
        songName: "Go Bananas",
        albumName: "Go Bananas",
        bandName: "Little Big",
      },
      {
        songName: "Miracle",
        albumName: "Exile",
        bandName: "Hurts",
      },
      {
        songName: "In The End",
        albumName: "Wretched and Divine: The Story of the Wild Ones",
        bandName: "Black Veil Brides",
      },
      {
        songName: "Ashes of Eden",
        albumName: "Dark Before Dawn",
        bandName: "Breaking Benjamin",
      },
      {
        songName: "Wolf Totem",
        albumName: "The Gereg",
        bandName: "The HU ft. Papa Roach",
      },
      {
        songName: "Blinding Lights",
        albumName: "After Hours",
        bandName: "The Weeknd",
      },
      {
        songName: "Roaring 20s",
        albumName: "Pray for the Wicked",
        bandName: "Panic! At The Disco",
      },
    ];

    return (
      <div className={classes.root}>
        <Typography variant="h3">
          Top {topSongs.length} searched songs.
        </Typography>
        <EnumeratedList listItems={topSongs} styleClasses={classes} />
      </div>
    );
  }
}

RatingList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(RatingList);
