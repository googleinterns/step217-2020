import { withStyles } from "@material-ui/styles";
import Tooltip from '@material-ui/core/Tooltip';

let LargeTextTooltip = withStyles({
  tooltip: {
	fontSize: "14px",
  }
})(Tooltip);

export default LargeTextTooltip;