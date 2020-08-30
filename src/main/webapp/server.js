// These import necessary modules and set some initial variables
require("dotenv").config();
const express = require("express");
const app = express();
const axios = require("axios");
const port = 5000;

// Test route, visit localhost:5000 to confirm it's working
// should show 'Hello World!' in the browser
app.get("/", (req, res) => res.send("Hello Worldв!"));

app.get("/api/youtube", async (req, res) => {
    try {
        const queryForSearch = req.query.q;
        const maxResults = req.query.maxResults;

        axios.get("https://www.googleapis.com/youtube/v3/search?" +
            "&q=" + queryForSearch + "&maxResults=" + maxResults +
            "&order=viewCount&type=video&key=" + process.env.API_KEY)
            .then(result => result.data)
            .then(response =>
                response.items.map((videoResult) => videoResult.id.videoId))
            .then(videos => {
                return res.json({
                    success: true,
                    videoIds: videos
                })
            });
    } catch (err) {
        return res.status(500).json({
            success: false,
            message: err.message,
        });
    }
});

// This spins up our sever and generates logs for us to use.
// Any console.log statements you use in node for debugging will show up in your
// terminal, not in the browser console!
app.listen(port, () => console.log(`Node JS server listening on port ${port}!`));