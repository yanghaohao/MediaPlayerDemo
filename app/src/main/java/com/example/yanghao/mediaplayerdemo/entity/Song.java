package com.example.yanghao.mediaplayerdemo.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable{
    private String title , path , artist , album ;
    private long duration ;

    public Song(String title, String path, String artist, String album , long duration) {
        super();
        this.title = title;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.duration = duration ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "MyAudio [title=" + title + ", path=" + path + ", artist="
                + artist + ", album=" + album + ", duration=" + duration
                + "]";
    }
}