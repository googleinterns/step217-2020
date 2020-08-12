package com.google.alpollo;

public class Song {
    private final String artist;
    private final String name;
    private final String album;

    public Song(String artist, String name, String album) {
        this.artist = artist;
        this.name = name;
        this.album = album;
    }

    public String artist() {
        return artist;
    }

    public String name() {
        return name;
    }

    public String album() {
        return album;
    }
}