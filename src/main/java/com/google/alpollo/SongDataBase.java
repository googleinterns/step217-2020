package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

public class SongDataBase {
    public static List<Song> topSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("The Weeknd", "Blinding Lights", "some text"));
        songs.add(new Song("Drake", "Toosie Slide", "another text"));
        return songs;
    }
}