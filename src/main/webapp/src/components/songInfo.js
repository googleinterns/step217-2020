import React from "react";
import PropTypes from "prop-types";
import YouTube from "react-youtube";
import { withStyles } from "@material-ui/styles";
import Typography from "@material-ui/core/Typography";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";

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
    whiteSpace: "pre-wrap",
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
  render() {
    const classes = this.props.classes;

    // TODO Fetch song information from database.
    const songInfo = {
      bandName: "David Bowie",
      songName: "Life On Mars?",
      lyrics:
        "It's a God-awful small affair\n" +
        "To the girl with the mousy hair\n" +
        "But her mummy is yelling no\n" +
        "And her daddy has told her to go\n" +
        "But her friend is nowhere to be seen\n" +
        "Now she walks through her sunken dream\n" +
        "To the seat with the clearest view\n" +
        "And she's hooked to the silver screen\n" +
        "But the film is a saddening bore\n" +
        "For she's lived it ten times or more\n" +
        "She could spit in the eyes of fools\n" +
        "As they ask her to focus on\n" +
        "Sailors fighting in the dance hall\n" +
        "Oh man, look at those cavemen go\n" +
        "It's the freakiest show\n" +
        "Take a look at the lawman\n" +
        "Beating up the wrong guy\n" +
        "Oh man, wonder if he'll ever know\n" +
        "He's in the best selling show\n" +
        "Is there life on Mars?\n" +
        "It's on America's tortured brow\n" +
        "That Mickey Mouse has grown up a cow\n" +
        "Now the workers have struck for fame\n" +
        "'Cause Lennon's on sale again\n" +
        "See the mice in their million hordes\n" +
        "From Ibiza to the Norfolk Broads\n" +
        "Rule Britannia is out of bounds\n" +
        "To my mother, my dog, and clowns\n" +
        "But the film is a saddening bore\n" +
        "'Cause I wrote it ten times or more\n" +
        "It's about to be writ again\n" +
        "As I ask you to focus on\n" +
        "Sailors fighting in the dance hall\n" +
        "Oh man, look at those cavemen go\n" +
        "It's the freakiest show\n" +
        "Take a look at the lawman\n" +
        "Beating up the wrong guy\n" +
        "Oh man, wonder if he'll ever know\n" +
        "He's in the best selling show\n" +
        "Is there life on Mars?",
      sentimentAnalysis: {
        score: "-0.6",
        magnitude: "1.3",
      },
      entityAnalysis: [
        {
          word: "affair",
          salience: 0.51,
          category: "work of art",
        },
        {
          word: "girl",
          salience: 0.09,
          category: "person",
        },
        {
          word: "film",
          salience: 0.06,
          category: "work of art",
        },
        {
          word: "selling show",
          salience: 0.04,
          category: "work of art",
        },
        {
          word: "God",
          salience: 0.01,
          category: "person",
        },
        {
          word: "Mars",
          salience: 0.01,
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

    function drawChart() {
      const data = new google.visualization.DataTable();
      data.addColumn('string', 'Name');
      data.addColumn('number', 'Salience');
      songInfo.entityAnalysis.forEach((entity) => {
        data.addRow([entity.word, entity.salience]);
      });

      const options = {
        'title': 'Most important words',
        'width':600,
        'height':500
      };

      const entityChart = new google.visualization.PieChart(document.getElementById('chart-container'));
      entityChart.draw(data, options);
    }

    function loadChartAPI() {
      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawChart);
    }

    // TODO display entities with charts
    return (
      <div className={classes.root}>
        <Typography variant="h3" className={classes.songName}>
          "{songInfo.songName}"
        </Typography>
        <div className={classes.languageAnalysisSection}>
          <div className={classes.songLyrics}>
            <Typography variant="h4">Lyrics</Typography>
            <p>{songInfo.lyrics}</p>
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