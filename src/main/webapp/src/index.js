import React from "react";
import ReactDOM from "react-dom";

fetch("/hello").then(() => {
  ReactDOM.render(
    <React.StrictMode>
      <p>Hi!</p>
    </React.StrictMode>,
    document.getElementById("root")
  );
});