import { withStyles } from "@material-ui/styles";
import TextField from '@material-ui/core/TextField';

let StyledTextField = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "black",
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: "#733F94",
    },
    "& .MuiOutlinedInput-root": {
      "&.Mui-focused fieldset": {
        borderColor: "#733F94",
      },
    },
  },
})(TextField);

export default StyledTextField;