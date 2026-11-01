package org.phobos.core.media.spotify;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.phobos.core.media.MediaTrack;

public class SpotifyTrack {
   @SerializedName("preview_url")
   public Object object;
   public String text;
   public String name;
   @SerializedName("disc_number")
   public Integer integer;
   @SerializedName("external_urls")
   public SpotifyLink spotifyLink;
   @SerializedName("external_ids")
   public SpotifyUrl spotifyUrl;
   @SerializedName("available_markets")
   public List<Object> objectList;
   public String text2;
   public String text3;
   @SerializedName("duration_ms")
   public Integer integer2;
   public SpotifyAlbum spotifyAlbum;
   public Boolean booleanValue;
   public String text4;
   @SerializedName("track_number")
   public Integer integer3;
   public Integer integer4;
   @SerializedName("is_local")
   public Boolean booleanValue2;
   public List<SpotifyEntity> spotifyEntityList = null;
   public transient String text5;

   public SpotifyTrack() {
      this.objectList = null;
   }

   public String describeText(String text) {
      if (this.spotifyEntityList != null && !this.spotifyEntityList.isEmpty()) {
         StringBuilder stringBuilder = new StringBuilder();

         for (SpotifyEntity spotifyEntity : this.spotifyEntityList) {
            stringBuilder.append(text);
            stringBuilder.append(spotifyEntity.name);
         }

         return stringBuilder.substring(text.length());
      } else {
         return null;
      }
   }

   public MediaTrack createMediaTrack() {
      return new MediaTrack(this.text2, this.name, this.describe(), this.integer2);
   }

   public String describe() {
      if (this.text5 == null) {
         this.text5 = this.describeText(", ");
      }

      return this.text5;
   }
}
