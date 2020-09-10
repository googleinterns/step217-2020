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

  /** 
   * Get entity analysis info if it was sent before
   * or load it instead.
   */
  componentDidMount() {
    if (this.props.sentInfo.wasSent) {
      this.setState({ entityAnalysisInfo: this.props.sentInfo.info });
    } else {
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

    /** Array that holds the data that will be displayed by the chart. */
    var top10WordsData = [];
    /** Array that will hold the wiki links for later use. */
    var top10WordsLinks = [];
    /** 
     * Create a custom tooltip to overwrite the default one, we want to show whether a wiki link
     * is available, besides the default data.
     * The p tells the tooltip to have the HTML property, so we can format the text.
     * https://developers.google.com/chart/interactive/docs/customizing_tooltip_content#customizing-html-content
     */
    const customTooltip = { 'role': 'tooltip', 'type': 'string', 'p': {'html': true} };

    /** 
     * React requires the first item to contain the title of each axis.
     * 
     * In our case, we have a pie chart without axes, but the first 2 elements
     * of the subarray are considered to be the x and y of the graph.
     * The third element of the first subarray is optional, but it tells the chart that we are going to
     * overwrite the default tooltip.
     * 
     * The next items of the array must follow the same pattern as in the first item.
     * (entity name and type, salience, custom tooltip)
     * 
     * You can think of it as a table, where the first row contains the titles of each column.
     * https://react-google-charts.com/pie-chart
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