import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import CircularProgress from "@material-ui/core/CircularProgress";
import objectEquals from '../helpers/objectEquals';
import axios from "axios";
import Chart from "react-google-charts";
import Typography from "@material-ui/core/Typography";

const styles = (theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
  },
});

/**
 * Displays entity analysis component for song lyrics.
 */
class EntityAnalysisInfo extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      entityAnalysisInfo: [],
      isLoading: false,
      error: null,
    };
  }

  /* If state has changed, send it to songInfo component */
  componentDidUpdate(prevProps, prevState) {
    if (!objectEquals(prevState, this.state)) {
      this.props.onChangeState(this.state);
    }
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .post("/entity", {
          lyrics: this.props.lyrics
      })
      .then((result) => result.data)
      .then((entityAnalysisInfo) =>
        this.setState({
          entityAnalysisInfo: entityAnalysisInfo,
          isLoading: false,
          error: null
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

    if (this.state.entityAnalysisInfo.length == 0) {
      return <p>No entities were found.</p>;
    }

    /** Generate a custom tooltip for each entity */
    function getTooltip(entity) {
      return '<div style="font-size:15px">' + entity.name + '<br>' 
          + '<b>Type: </b>' + entity.type + '<br>' 
          + '<b>Salience: </b>' + entity.salience + '<br>' 
          + ((entity.wikiLink !== "") ? 'Click for wiki link' : '')
          + '</div>';
    }

    var top10WordsData = [];
    var top10WordsLinks = [];
    /** 
     * Create a custom tooltip to overwrite the default one, we want to show whether a wiki link
     * is available, besides the default data.
     * This tooltip will be able to interpret HTML, so we can format the text.
     */
    const customTooltip = { role: "tooltip", type: "string", 'p': {'html': true} };

    /** 
     * Gather entity data in a separate array to display later in the chart.
     * We make a separate array for wiki links to use them later for redirection.
     */
    top10WordsData.push(['Word and Type', 'Importance', customTooltip])
    this.state.entityAnalysisInfo.forEach((entity) => {
      top10WordsData.push([entity.name + ' (' + entity.type + ')', entity.salience, getTooltip(entity)]);
      top10WordsLinks.push(entity.wikiLink);
    })

    const chartEvents = [
      {
        eventName: "select",
        callback({ chartWrapper }) {
          const [selectedItem] = chartWrapper.getChart().getSelection();
          const { row } = selectedItem;

          if (top10WordsLinks[row] !== "") {
            window.location.href = top10WordsLinks[row];
          }
        }
      }
    ];

    var offsetSlices = [];
    for (var i = 0; i < top10WordsLinks.length; i++) {
      if (top10WordsLinks[i] !== "") {
        offsetSlices[i] = { offset: 0.1 };
      }
    }

    return (
      <div>
        <div style={{ display: 'flex', maxWidth: 900 }}>
          <Chart
            width={1200}
            height={400}
            chartType="PieChart"
            loader={<div>Loading Chart</div>}
            data={top10WordsData}
            options={{
              title: 'Most Important Words',
              legend: { viewWindow: { min: 0, max: 15 } },
              slices: offsetSlices,
              focusTarget: 'category',
              tooltip: { isHtml: true }
            }}
            chartEvents={chartEvents}
            legendToggle
          />
        </div>
      </div>
    );
  }
}

EntityAnalysisInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EntityAnalysisInfo);