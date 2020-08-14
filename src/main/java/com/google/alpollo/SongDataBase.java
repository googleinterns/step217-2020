package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

/** Class provides methods to work with the database. */
public class SongDataBase {
    /** Returns the list of the most requested songs. */
    public static List<Song> topSongs() {
        // Static songs now. TODO: automate taking songs from the database.
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("The Weeknd", "Blinding Lights", "After Hours"));
        songs.add(new Song("Drake", "Toosie Slide", "Dark Lane Demo Tapes"));
        return songs;
    }
}