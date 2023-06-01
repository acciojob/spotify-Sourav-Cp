package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);

        List<Playlist> plays = new ArrayList<>();
        userPlaylistMap.put(user,plays);

        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);

        List<Album> list = new ArrayList<>();
        artistAlbumMap.put(artist,list);

        return artist;
    }

    public Artist findArtist(String artistName)
    {
        for(Artist artist : artists)
        {
            if(artist.getName().equals(artistName))
            {
                return artist;
            }
        }
        return null;
    }
    public Album createAlbum(String title, String artistName) {
        Artist artist = findArtist(artistName);
        Album album = new Album(title);

        if(artist == null)
        {
            artist = new Artist(artistName);
            artists.add(artist);
            List<Album> newList = new ArrayList<>();
            newList.add(album);
            artistAlbumMap.put(artist,newList);
        }

        else
        {
          List<Album> oldList = artistAlbumMap.get(artist);
          oldList.add(album);
          artistAlbumMap.put(artist,oldList);
        }

        albums.add(album);
        List<Song> song = new ArrayList<>();
        albumSongMap.put(album,song);

        return album;
    }

    public Album findAlbum(String albumName)
    {
        for(Album album : albums)
        {
            if(album.getTitle().equals(albumName))
            {
                return album;
            }
        }
        return null;
    }
    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album = findAlbum(albumName);
        if(album == null)
        {
            throw new Exception("Album does not exist");
        }

        Song song = new Song(title,length);
        List<User> user = new ArrayList<>();
        songLikeMap.put(song,user);

        songs.add(song);
        List<Song> songList = albumSongMap.get(album);
        songList.add(song);
        albumSongMap.put(album,songList);

        return song;
    }
    public User findUser(String mobile)
    {
        for(User user : users)
        {
            if(user.getMobile().equals(mobile))
            {
                return user;
            }
        }
        return null;
    }
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
      User user = findUser(mobile);
      if(user == null)
      {
          throw new Exception("User does not exist");
      }

      Playlist playlist = new Playlist(title);
      List<Song> songList = new ArrayList<>();

      for(Song song : songs)
      {
          if(song.getLength() == length)
          {
              songList.add(song);
          }
      }
      playlistSongMap.put(playlist,songList);
      creatorPlaylistMap.put(user,playlist);

      List<User> listener = new ArrayList<>();
      listener.add(user);
      playlistListenerMap.put(playlist,listener);

      List<Playlist> lists = userPlaylistMap.get(user);
      lists.add(playlist);
      userPlaylistMap.put(user,lists);
      playlists.add(playlist);

      return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = findUser(mobile);
        if(user == null)
        {
            throw new Exception("User does not exist");
        }

        Playlist playlist = new Playlist(title);
        List<Song> songList = new ArrayList<>();

        for(Song song : songs)
        {
            if(songTitles.contains(song.getTitle()))
            {
                songList.add(song);
            }
        }
        playlistSongMap.put(playlist,songList);
        creatorPlaylistMap.put(user,playlist);

        List<User> listener = new ArrayList<>();
        listener.add(user);
        playlistListenerMap.put(playlist,listener);

        List<Playlist> lists = userPlaylistMap.get(user);
        lists.add(playlist);
        userPlaylistMap.put(user,lists);
        playlists.add(playlist);

        return playlist;
    }

    public Playlist findPlayList(String playListTitle)
    {
        for (Playlist playlist : playlists)
        {
            if(playlist.getTitle().equals(playListTitle))
            {
                return playlist;
            }
        }
        return null;
    }
    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
      User user = findUser(mobile);
      if(user == null)
      {
          throw new Exception("User does not exist");
      }

      Playlist playlist = findPlayList(playlistTitle);
      if(playlist == null)
      {
          throw new Exception("Playlist does not exist");
      }

      List<User> userList = playlistListenerMap.get(playlist);
      if(! userList.contains(user))
      {
          userList.add(user);
          playlistListenerMap.put(playlist,userList);

          List<Playlist> playlistList = userPlaylistMap.get(user);
          playlistList.add(playlist);
          userPlaylistMap.put(user,playlistList);
      }
      return playlist;
    }

    public Song findSong(String songTitle)
    {
        for(Song song : songs)
        {
            if(song.getTitle().equals(songTitle))
            {
                return song;
            }
        }
        return null;
    }
    public Album findSongsAlbum(String songTitle)
    {
        for(Album album : albumSongMap.keySet())
        {
            List<Song> song = albumSongMap.get(album);
            for (Song song1 : song)
            {
                if(song1.getTitle().equals(songTitle))
                {
                    return album;
                }
            }
        }
        return null;
    }
    public Artist findAlbumArtist(Album album)
    {
        for(Artist artist : artistAlbumMap.keySet())
        {
            List<Album> albums1 = artistAlbumMap.get(artist);
            for (Album album1 : albums1)
            {
                if(album1.equals(album))
                {
                    return artist;
                }
            }
        }
        return null;
    }
    public Song likeSong(String mobile, String songTitle) throws Exception {
     User user = findUser(mobile);
     if(user == null)
     {
         throw new Exception("User does not exist");
     }
     Song song = findSong(songTitle);
     if(song == null)
     {
         throw new Exception("Song does not exist");
     }
      Album album = findSongsAlbum(songTitle);
      Artist artist = findAlbumArtist(album);

      if(songLikeMap.containsKey(song))
      {
          List<User> userList = songLikeMap.get(song);
          if(! userList.contains(user))
          {
              userList.add(user);
              song.setLikes(song.getLikes()+1);
              artist.setLikes(artist.getLikes()+1);
              songLikeMap.put(song,userList);
          }
      }
      return song;
    }

    public String mostPopularArtist() {
        Integer max = Integer.MIN_VALUE;
        String name = "";
        for(Artist artist : artists)
        {
            if(artist.getLikes() > max)
            {
                max = artist.getLikes();
                name = artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        Integer max = Integer.MIN_VALUE;
        String name = "";
        for(Song song : songs)
        {
            if(song.getLikes() > max)
            {
                max = song.getLikes();
                name = song.getTitle();
            }
        }
        return name;
    }
}
