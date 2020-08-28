import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/styles";
import CircularProgress from "@material-ui/core/CircularProgress";
import axios from "axios";
import Chart from "react-google-charts";

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
          <CircularProgress style={{ color: "black" }} />;
        </div>
      );
    }

    if (this.state.entityAnalysisInfo.length == 0) {
      return <p>No entities were found.</p>;
    }

       /** Gather the most important words in a simple array */
       var top10WordsData = [];
       top10WordsData.push(['Word and Type', 'Importance'])
       this.state.entityAnalysisInfo.forEach((entity) => {
           top10WordsData.push([entity.name + ' (' + entity.type + ')', entity.salience]);
       })
     
 
     return (
       <div style={{ display: 'flex', maxWidth: 900 }}>
         <Chart
           width={600}
           height={600}
           chartType="PieChart"
           loader={<div>Loading Chart</div>}
           data={top10WordsData}
           options={{
             title: 'Most Important Words',
           }}
           legendToggle
         />
       </div>
     );
   }
 }

EntityAnalysisInfo.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EntityAnalysisInfo);
