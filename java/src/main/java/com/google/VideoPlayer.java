package com.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private VideoPlaylist videoPlaylist;
  private List<VideoPlaylist> listOfPlaylists = new ArrayList<>();

  private Boolean isPlaying = false, isPaused = false;
  private String flagReason = "Not supplied";
  private Video currentVideo, newVideo;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    ArrayList<Video> videos = new ArrayList<>();
    for(Video video : videoLibrary.getVideos()){
      videos.add(video);
    }
    videos = SortVideosInLexicographicalOrder(videos);

    System.out.println("Here's a list of all available videos:");
    for(int i = 0; i < videos.size(); ++i) {
      System.out.print(
        videos.get(i).getTitle()+" ("+
        videos.get(i).getVideoId()+") "+
        videos.get(i).getTags().toString().replace(",", ""));
      if(videos.get(i).getFlagStatus()){
        System.out.println(" - FLAGGED (reason: "+flagReason+")");
      }
      else{
        System.out.println();
      }
    }
  }

  public void playVideo(String videoId) {
    if(doesVideoExist(videoId)){
      for(Video video : videoLibrary.getVideos()){
        if(video.getVideoId().contains(videoId)){
          if(!video.getFlagStatus()){
            if(isPlaying) {
              currentVideo = newVideo;
              System.out.println("Stopping video: " + currentVideo.getTitle());
            }
            newVideo = video;
            currentVideo = newVideo;
            System.out.println("Playing video: " + newVideo.getTitle());
            isPlaying = true;
            isPaused = false;
          }
          else{
            System.out.println("Cannot play video: Video is currently flagged (reason: "+flagReason+ ")");
          }
        }
      }
    }
    else{
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(!isPlaying){
      System.out.println("Cannot stop video: No video is currently playing");
    }
    else{
      System.out.println("Stopping video: " + currentVideo.getTitle());
    }
    isPlaying = false;
  }

  public void playRandomVideo() {
    Boolean videosAvailable = false;

    Random random = new Random();
    int max = videoLibrary.getVideos().size()-1;
    int min = 0;
    int randomNumber = random.nextInt(max-min +1)+min;

    List<Video> videos = videoLibrary.getVideos();
    for(Video video : videos){
      if(!video.getFlagStatus()){
        videosAvailable = true;
      }
    }
    if(videosAvailable){
      String videoId = videos.get(randomNumber).getVideoId();
      playVideo(videoId);
    }
    else{
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
    if(currentVideo==null||currentVideo.getTitle().isEmpty()){
      System.out.println("Cannot pause video: No video is currently playing");
    }
    else if(isPaused){
      System.out.println("Video already paused: " + currentVideo.getTitle());
    }
    else{
      System.out.println("Pausing video: " + currentVideo.getTitle());
      isPaused = true;
    }
  }

  public void continueVideo() {
    if(isPaused){
      isPaused=false;
      System.out.println("Continuing video: " + currentVideo.getTitle());
    }
    else if(!isPlaying){
      System.out.println("Cannot continue video: No video is currently playing");
    }
    else{
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {
    ArrayList<Video> videos = new ArrayList<>();
    for(Video video : videoLibrary.getVideos()){
      videos.add(video);
    }
    if(isPlaying&!isPaused){
      System.out.println(
        "Currently playing: "+
        currentVideo.getTitle()+" ("+
        currentVideo.getVideoId()+") "+
        currentVideo.getTags().toString().replace(",", ""));
    }
    else if(isPaused){
      try{
        if(!currentVideo.getFlagStatus()){
          System.out.println(
                  "Currently playing: "+
                          currentVideo.getTitle()+" ("+
                          currentVideo.getVideoId()+") "+
                          currentVideo.getTags().toString().replace(",", "")+
                          " - PAUSED");
        }
      }
      catch(Exception e){

      }
    }
    else{
     System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    Boolean playlistAlreadyExists = false;
    for(VideoPlaylist videoPlaylist:listOfPlaylists){
      if(videoPlaylist.getPlaylistName().toLowerCase().equals(playlistName.toLowerCase())){
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        playlistAlreadyExists = true;
      }
    }
    if(!playlistAlreadyExists){
      videoPlaylist = new VideoPlaylist(playlistName);
      listOfPlaylists.add(videoPlaylist);
      System.out.println("Successfully created new playlist: " + videoPlaylist.getPlaylistName());
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if(doesPlaylistExist(playlistName.toLowerCase())){
      if(doesVideoExist(videoId)){
        for(VideoPlaylist videoPlaylist:listOfPlaylists){
          if(videoPlaylist.getPlaylistName().toLowerCase().equals(playlistName.toLowerCase())){
            for(Video video : videoLibrary.getVideos()){
              if(video.getVideoId().contains(videoId)){
                if(!video.getFlagStatus()){
                  if(!videoPlaylist.getVideosFromPlayList().contains(video)){
                    videoPlaylist.addVideo(video);
                    System.out.println("Added video to "+ playlistName + ": " + video.getTitle());
                  }
                  else{
                    System.out.println("Cannot add video to "+ playlistName + ": " + "Video already added");
                  }
                }
                else{
                  System.out.println("Cannot add video to my_playlist: Video is currently flagged (reason: "+flagReason+ ")");
                }
              }
            }
          }
        }
      }
      else{
        System.out.println("Cannot add video to "+ playlistName + ": " + "Video does not exist");
      }
    }
    else{
      System.out.println("Cannot add video to "+ playlistName + ": " + "Playlist does not exist");
    }
  }

  public void showAllPlaylists() {
    if(listOfPlaylists.size()<1){
      System.out.println("No playlists exist yet");
    }
    else{
      System.out.println("Showing all playlists:");
      listOfPlaylists = SortPlaylistsInLexicographicalOrder(listOfPlaylists);
      for(VideoPlaylist videoPlaylist:listOfPlaylists){
        System.out.println(videoPlaylist.getPlaylistName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    if(doesPlaylistExist(playlistName.toLowerCase())){
      for(VideoPlaylist videoPlaylist:listOfPlaylists) {
        if (videoPlaylist.getPlaylistName().toLowerCase().equals(playlistName.toLowerCase())) {
          System.out.println("Showing playlist: " + playlistName);
          if(videoPlaylist.getVideosFromPlayList().size()<1){
            System.out.println("No videos here yet");
          }
          else{
            for(Video video: videoPlaylist.getVideosFromPlayList()){
              System.out.print(
                video.getTitle()+" ("+
                video.getVideoId()+") "+
                video.getTags().toString().replace(",", ""));
              if(video.getFlagStatus()){
                System.out.println(" - FLAGGED (reason: "+flagReason+")");
              }
              else{
                System.out.println();
              }
            }
          }
        }
      }
    }
    else{
      System.out.println("Cannot show playlist "+ playlistName + ": " + "Playlist does not exist");
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if(doesPlaylistExist(playlistName.toLowerCase())) {
      if(doesVideoExist(videoId)){
        if(doesVideoExistInPlaylist(videoId)){
          Video videoToRemove = null;
          for(Video video : videoPlaylist.getVideosFromPlayList()){
            if(video.getVideoId().contains(videoId)){
              videoToRemove = video;
            }
          }
          System.out.println("Removed video from "+playlistName+": "+videoToRemove.getTitle());
          videoPlaylist.removeVideo(videoToRemove);
        }
        else{
          System.out.println("Cannot remove video from "+ playlistName + ": " + "Video is not in playlist");
        }
      }
      else{
        System.out.println("Cannot remove video from "+ playlistName + ": " + "Video does not exist");
      }
    }
    else{
      System.out.println("Cannot remove video from "+ playlistName + ": " + "Playlist does not exist");
    }
  }

  public void clearPlaylist(String playlistName) {
    if(doesPlaylistExist(playlistName.toLowerCase())) {
      if(videoPlaylist.getVideosFromPlayList().size()>0){
        videoPlaylist.removeAllVideos();
        System.out.println("Successfully removed all videos from " + playlistName);
      }
    }
    else{
      System.out.println("Cannot clear playlist "+ playlistName + ": " + "Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    if(doesPlaylistExist(playlistName.toLowerCase())) {
      listOfPlaylists.remove(playlistName);
      System.out.println("Deleted playlist: "+ playlistName);
    }
    else{
      System.out.println("Cannot delete playlist "+ playlistName + ": " + "Playlist does not exist");
    }
  }

  public void searchVideos(String searchTerm) {
    ArrayList<Video> videos = new ArrayList<>();
    for(Video video : videoLibrary.getVideos()){
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
        if(!video.getFlagStatus()){
          videos.add(video);
        }
      }
    }
    if(videos.size() >0){
      videos = SortVideosInLexicographicalOrder(videos);
      System.out.println("Here are the results for "+searchTerm+":");
      for(int i = 0; i < videos.size(); ++i) {
        System.out.println(
          i+1+") "+
          videos.get(i).getTitle()+" ("+
          videos.get(i).getVideoId()+") "+
          videos.get(i).getTags().toString().replace(",", ""));
      }

      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");

      try{
        int userInput = Run.scanner.nextInt();
        if(userInput<=videos.size()&userInput>0){
          System.out.println("Playing video: "+ videos.get(userInput-1).getTitle());
        }
      }
      catch(Exception e){

      }
    }
    else{
     System.out.println("No search results for "+searchTerm);
    }
    Run.scanner.close();
  }

  public void searchVideosWithTag(String videoTag) {
    String searchTerm = videoTag.replace("#","");
    ArrayList<Video> videos = new ArrayList<>();
    for(Video video : videoLibrary.getVideos()){
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
        if(!video.getFlagStatus()){
          videos.add(video);
        }
      }
    }
    if(videos.size() >0){
      videos = SortVideosInLexicographicalOrder(videos);
      System.out.println("Here are the results for "+videoTag+":");
      for(int i = 0; i < videos.size(); ++i) {
        System.out.println(
          i+1+") "+
          videos.get(i).getTitle()+" ("+
          videos.get(i).getVideoId()+") "+
          videos.get(i).getTags().toString().replace(",", ""));
      }

      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");

      try{
        int userInput = Run.scanner.nextInt();
        if(userInput<=videos.size()&userInput>0){
          System.out.println("Playing video: "+ videos.get(userInput-1).getTitle());
        }
      }
      catch(Exception e){

      }
    }
    else{
      System.out.println("No search results for "+videoTag);
    }
    Run.scanner.close();
  }

  public void flagVideo(String videoId) {
    if(doesVideoExist(videoId)){
      for(Video video : videoLibrary.getVideos()){
        if(video.getVideoId().contains(videoId)){
          if(!video.getFlagStatus()){
            if(isPlaying||isPaused){
              if(currentVideo.equals(video)){
                stopVideo();
              }
            }
              video.setFlagStatus(true);
              System.out.println("Successfully flagged video: " + video.getTitle() +
                      " (reason: " + flagReason + ")");
          }
          else{
            System.out.println("Cannot flag video: Video is already flagged");
          }
        }
      }
    }
    else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void flagVideo(String videoId, String reason) {
    if(doesVideoExist(videoId)){
      for(Video video : videoLibrary.getVideos()){
        if(video.getVideoId().contains(videoId)){
          if(!video.getFlagStatus()){
            if(isPlaying||isPaused){
              if(currentVideo.equals(video)){
                stopVideo();
              }
            }
              video.setFlagStatus(true);
              flagReason = reason;
              System.out.println("Successfully flagged video: " + video.getTitle() +
                      " (reason: " + flagReason + ")");
          }
          else{
            System.out.println("Cannot flag video: Video is already flagged");
          }
        }
      }
    }
    else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    if(doesVideoExist(videoId)){
      for(Video video : videoLibrary.getVideos()){
        if(video.getVideoId().contains(videoId)){
          if(video.getFlagStatus()){
            video.setFlagStatus(false);
            System.out.println("Successfully removed flag from video: " + video.getTitle() + ")");
          }
          else if(!video.getFlagStatus()){
            System.out.println("Cannot remove flag from video: Video is not flagged");
          }
          else{
            System.out.println("Cannot flag video: Video is already flagged");
          }
        }
      }
    }
    else{
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }



  // additional methods
  private ArrayList<Video> SortVideosInLexicographicalOrder(ArrayList<Video> videos){
    for(int i = 0; i < videos.size()-1; ++i) {
      for (int j = i + 1; j < videos.size(); ++j) {
        if (videos.get(i).getTitle().compareTo(videos.get(j).getTitle()) > 0) {
          Video temp = videos.get(i);
          videos.set(i, videos.get(j));
          videos.set(j, temp);
        }
      }
    }
    return videos;
  }

  private List<VideoPlaylist> SortPlaylistsInLexicographicalOrder(List<VideoPlaylist> listOfPlaylists){
    for(int i = 0; i < listOfPlaylists.size()-1; ++i) {
      for (int j = i + 1; j < listOfPlaylists.size(); ++j) {
        if (listOfPlaylists.get(i).getPlaylistName().compareTo(listOfPlaylists.get(j).getPlaylistName()) > 0) {
          VideoPlaylist temp = listOfPlaylists.get(i);
          listOfPlaylists.set(i, listOfPlaylists.get(j));
          listOfPlaylists.set(j, temp);
        }
      }
    }
    return listOfPlaylists;
  }

  private Boolean doesVideoExist(String videoId){
    List<String> ids = new ArrayList<>();

    for(Video video : videoLibrary.getVideos()){
      ids.add(video.getVideoId());
    }

    Boolean videoIdExists = (ids.contains(videoId))? true:false;

    return videoIdExists;
  }

  private Boolean doesPlaylistExist(String playlistName){
    List<String> names = new ArrayList<>();

    for(VideoPlaylist videoPlaylist:listOfPlaylists){
      names.add(videoPlaylist.getPlaylistName().toLowerCase());
    }

    Boolean playlistExists = (names.contains(playlistName.toLowerCase()))?true:false;

    return playlistExists;
  }

  private Boolean doesVideoExistInPlaylist(String videoId){
    List<String> ids = new ArrayList<>();

    for(Video video : videoPlaylist.getVideosFromPlayList()){
      ids.add(video.getVideoId());
    }

    Boolean videoExistsInPlaylist = (ids.contains(videoId))?true:false;

    return videoExistsInPlaylist;
  }
}

