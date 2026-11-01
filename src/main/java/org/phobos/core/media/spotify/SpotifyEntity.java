package org.phobos.core.media.spotify;

import com.google.gson.annotations.SerializedName;

public class SpotifyEntity {
   public String text;
   public String text2;
   @SerializedName("external_urls")
   public SpotifyLink spotifyLink;
   public String name;
   public String text3;
   public String text4;
}
