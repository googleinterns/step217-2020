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

    /** Gather the most important words and their data in simple arrays. */
    var top10WordsData = [];
    var top10WordsLinks = [];
    top10WordsData.push(['Word and Type', 'Importance'])
    this.state.entityAnalysisInfo.forEach((entity) => {
      top10WordsData.push([entity.name + ' (' + entity.type + ')', entity.salience]);
      top10WordsLinks.push(entity.wikiLink);
    })

    const chartEvents = [
      {
        eventName: "select",
        callback({ chartWrapper }) {
          const [selectedItem] = chartWrapper.getChart().getSelection();
          const { row, column } = selectedItem;

          if(top10WordsLinks[row] !== "") {
            window.location.href = top10WordsLinks[row];
          }
        }
      }
    ];

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
              legend: {viewWindow: { min: 0, max: 15 }},
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