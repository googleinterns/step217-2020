import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import CircularProgress from "@material-ui/core/CircularProgress";
import objectsEqual from '../helpers/objectEquals';
import axios from "axios";

const google = window.google;

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
    if (!objectsEqual(prevState, this.state)) {
      this.props.onChangeState(this.state);
    }
  }

  componentDidMount() {
    this.setState({ isLoading: true });

    axios
      .post(
        "/entity",
        {},
        {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          },
          params: {
            lyrics: this.props.lyrics,
          },
        }
      )
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

    /**
     * Function will draw a pie chart with static data representing the most important words in
     * our song's context.
     */
    const drawTop10WordsChart = (element) => {
      const data = new google.visualization.DataTable();
      data.addColumn("string", "Name");
      data.addColumn("number", "Salience");
      this.state.entityAnalysisInfo.forEach((entity) => {
        data.addRow([entity.name, entity.salience]);
      });

      const options = {
        title: "Most important words",
        width: 600,
        height: 500,
      };

      const entityChart = new google.visualization.PieChart(element);
      entityChart.draw(data, options);
    }

    const loadChartAPI = (element) => {
      // Load the Visualization API and the corechart package.
      google.charts.load("current", { packages: ["corechart"] });

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(function () {
        drawTop10WordsChart(element);
      });
    }

    return (
      <div
        ref={(elem) => {
          if (elem) loadChartAPI(elem);
        }}
      ></div>
    );
  }
}

EntityAnalysisInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EntityAnalysisInfo);
