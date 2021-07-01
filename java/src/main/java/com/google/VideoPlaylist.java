package com.google;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String playlistName;
    private List<Video> videos = new ArrayList<>();

    VideoPlaylist(String playlistName){
        this.playlistName = playlistName;
    }

    public String getPlaylistName(){
        return this.playlistName;
    };

    public List<Video> getVideosFromPlayList(){
        return this.videos;
    };

    public void addVideo(Video video){
        videos.add(video);
    }

    public void removeVideo(Video video){
        videos.remove(video);
    }

    public void removeAllVideos(){
        videos.removeAll(videos);
    }
}
