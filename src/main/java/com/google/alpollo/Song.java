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

    public String getArtists() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }
}