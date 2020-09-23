import React from 'react';
import { withStyles } from "@material-ui/styles";
import Autocomplete from '@material-ui/lab/Autocomplete';
import Typography from '@material-ui/core/Typography';
import throttle from 'lodash/throttle';
import axios from "axios";
import StyledTextField from "../styledComponents/styledTextField";

export default function AutocompleteTextField(props) {
  const [value, setValue] = React.useState(props.value);
  const [options, setOptions] = React.useState([]);

  const fetch = React.useMemo(
    () =>
      throttle((request, callback) => {
        axios
          .post(`/autocomplete`, { type: props.type, searchString: request })
          .then((result) => result.data)
          .then((options) => callback(options));
      }, 200),
    [],
  );

  React.useEffect(() => {
    // Don't suggest anything if the value is empty.
    if (value === '') {
      return;
    }

    let active = true;

    fetch(value, (results) => {
      if (active) {
        let newOptions = [];

        if (value) {
          newOptions = [value];
        }

        if (results) {
          newOptions = [...newOptions, ...results];
        }

        setOptions(newOptions);
      }
    });

    return () => {
      active = false;
    };
  }, [value, fetch]);

  return (
    <Autocomplete
      className={props.className}
      id={props.id}
      options={options}
      freeSolo
      autoComplete
      includeInputInList
      filterSelectedOptions
      value={value}
      onChange={(event, newValue) => {
        setOptions(newValue ? [newValue, ...options] : options);
        setValue(newValue);
        props.handleChange(props.id, newValue);
      }}
      onInputChange={(event, newValue) => {
        setValue(newValue);    
        props.handleChange(props.id, newValue);
      }}
      renderInput={(params) => (
        <StyledTextField {...params} label={props.label} variant={props.variant} fullWidth />
      )}
      renderOption={(option) => <Typography noWrap>{option}</Typography>}
    />
  );
}
