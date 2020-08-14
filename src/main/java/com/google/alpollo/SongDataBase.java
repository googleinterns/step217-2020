package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

public class SongDataBase {
    public static List<Song> topSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("The Weeknd", "Blinding Lights", "After Hours"));
        songs.add(new Song("Drake", "Toosie Slide", "Dark Lane Demo Tapes"));
        return songs;
    }
}