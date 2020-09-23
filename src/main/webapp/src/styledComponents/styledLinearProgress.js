import { withStyles } from "@material-ui/styles";
import LinearProgress from '@material-ui/core/LinearProgress';
import { lighten } from '@material-ui/core/styles';

let StyledLinearProgress = withStyles({
  root: {
    backgroundColor: lighten('#000000', 0.5), 
  },
  bar: {
    background: '#000000',
  },
})(LinearProgress);

export default StyledLinearProgress;