import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import { ListItemIcon } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";
import axios from "axios";

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
        primary={item.song.name}
        secondary={item.song.artist}
      />
    </ListItem>
  ));
  return <List>{reactListItems}</List>;
}

class RatingList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      topSongs: [],
      isLoading: false,
      error: null,
    };
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .get("/top")
      .then((result) => result.data)
      .then((topSongs) =>
        this.setState({
          topSongs: topSongs,
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

  render() {
    const classes = this.props.classes;
    const topSongs = this.state.topSongs;

    if (this.state.error) {
      console.log(this.state.error.message);
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

    if (topSongs.length == 0) {
      return (
        <div className={classes.root}>
          <Typography variant="h3">
            No songs were searched for. Be the first!
          </Typography>
        </div>
      );
    }

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
