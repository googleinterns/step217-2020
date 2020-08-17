import React from "react";
import Navbar from "./components/Navbar";
import RatingList from "./components/RatingList";
import "./App.css";

export default function App() {
  return (
    <div className="AppMain">
      <Navbar />
      <RatingList />
    </div>
  );
}