package org.phobos.core.media.spotify;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpotifyAlbum {
   public List<SpotifyImage> spotifyImageList;
   public String text;
   @SerializedName("available_markets")
   public List<Object> objectList;
   public String name;
   public String text2;
   public List<SpotifyEntity> spotifyEntityList = null;
   public String text3;
   @SerializedName("external_urls")
   public SpotifyLink spotifyLink;
   @SerializedName("release_date_precision")
   public String text4;
   @SerializedName("release_date")
   public String text5;
   @SerializedName("total_tracks")
   public Integer integer;
   public String text6;
   @SerializedName("album_type")
   public String text7;

   public SpotifyAlbum() {
      this.objectList = null;
      this.spotifyImageList = null;
   }
}
