package org.phobos.core.media.spotify;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import org.phobos.core.media.MediaTrack;

public class SpotifyApiClient {
   public static final String text = "https://api.spotify.com/v1/tracks/%s";
   public final LimitedRegistry<BufferedImage> bufferedImageLimitedRegistry;
   public static final Gson gson = new Gson();
   public static final String text2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/71.0.3578.98";
   public SpotifyText spotifyText;
   public static final String text3 = "https://open.spotify.com/get_access_token?reason=transport&productType=web_player";
   public final LimitedRegistry<SpotifyTrack> spotifyTrackLimitedRegistry;
   public final Executor executor = Executors.newSingleThreadExecutor();

   public void handleText(String text, Consumer<String> consumer) {
      this.executor.execute(() -> {
         try {
            String text2 = this.describeText(text);
            if (text2 != null) {
               consumer.accept(text2);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      });
   }

   public BufferedImage resolveBufferedImage(MediaTrack mediaTrack) throws IOException {
      return this.resolveBufferedImage2(mediaTrack.getText());
   }

   public void handleMediaTrack(MediaTrack mediaTrack, Consumer<BufferedImage> consumer) {
      this.handleText3(mediaTrack.getText(), consumer);
   }

   public LimitedRegistry<SpotifyTrack> getSpotifyTrackLimitedRegistry() {
      return this.spotifyTrackLimitedRegistry;
   }

   public BufferedImage resolveBufferedImage2(String text) throws IOException {
      BufferedImage bufferedImage = this.bufferedImageLimitedRegistry.resolveObject(text);
      if (bufferedImage != null) {
         return bufferedImage;
      }

      String text2 = this.describeText(text);
      if (text2 == null) {
         return null;
      }

      BufferedImage bufferedImage2 = ImageIO.read(new URL(text2));
      if (bufferedImage2 == null) {
         throw new IOException("Could not load image: " + text2);
      }

      this.bufferedImageLimitedRegistry.handleText(text, bufferedImage2);
      return bufferedImage2;
   }

   public void handleMediaTrack2(MediaTrack mediaTrack, Consumer<SpotifyTrack> consumer) {
      this.handleText2(mediaTrack.getText(), consumer);
   }

   public SpotifyTrack resolveSpotifyTrack(MediaTrack mediaTrack) throws IOException {
      return this.resolveSpotifyTrack2(mediaTrack.getText());
   }

   public void handleText2(String text, Consumer<SpotifyTrack> consumer) {
      this.executor.execute(() -> {
         try {
            SpotifyTrack spotifyTrack = this.resolveSpotifyTrack2(text);
            if (spotifyTrack != null) {
               consumer.accept(spotifyTrack);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      });
   }

   public void handleConsumer(Consumer<SpotifyText> consumer) {
      this.executor.execute(() -> {
         try {
            consumer.accept(this.getSpotifyText());
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      });
   }

   public SpotifyApiClient() {
      this.bufferedImageLimitedRegistry = new LimitedRegistry<>(10);
      this.spotifyTrackLimitedRegistry = new LimitedRegistry<>(100);
   }

   public SpotifyText getSpotifyText() throws IOException {
      HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL("https://open.spotify.com/get_access_token?reason=transport&productType=web_player")
         .openConnection();
      httpsURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/71.0.3578.98");
      httpsURLConnection.addRequestProperty("referer", "https://open.spotify.com/");
      httpsURLConnection.addRequestProperty("app-platform", "WebPlayer");
      JsonReader jsonReader = new JsonReader(new InputStreamReader(httpsURLConnection.getInputStream()));
      return (SpotifyText)gson.fromJson(jsonReader, SpotifyText.class);
   }

   public SpotifyTrack resolveSpotifyTrack2(String text) throws IOException {
      SpotifyTrack spotifyTrack = this.spotifyTrackLimitedRegistry.resolveObject(text);
      if (spotifyTrack != null) {
         return spotifyTrack;
      }

      String text2 = String.format("https://api.spotify.com/v1/tracks/%s", text);
      SpotifyTrack spotifyTrack2 = this.resolveObject(text2, SpotifyTrack.class, true);
      this.spotifyTrackLimitedRegistry.handleText(text, spotifyTrack2);
      return spotifyTrack2;
   }

   public void handleText3(String text, Consumer<BufferedImage> consumer) {
      this.executor.execute(() -> {
         try {
            BufferedImage bufferedImage = this.resolveBufferedImage2(text);
            if (bufferedImage != null) {
               consumer.accept(bufferedImage);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      });
   }

   public LimitedRegistry<BufferedImage> getBufferedImageLimitedRegistry() {
      return this.bufferedImageLimitedRegistry;
   }

   public String describeText(String text) throws IOException {
      SpotifyTrack spotifyTrack = this.resolveSpotifyTrack2(text);
      return spotifyTrack == null ? null : spotifyTrack.spotifyAlbum.spotifyImageList.get(0).text;
   }

   public <T> T resolveObject(String text, Class<?> classValue, boolean flag) throws IOException {
      if (this.spotifyText == null) {
         this.spotifyText = this.getSpotifyText();
      }

      HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(text).openConnection();
      httpsURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/71.0.3578.98");
      httpsURLConnection.addRequestProperty("referer", "https://open.spotify.com/");
      httpsURLConnection.addRequestProperty("app-platform", "WebPlayer");
      httpsURLConnection.addRequestProperty("origin", "https://open.spotify.com");
      if (this.spotifyText != null) {
         httpsURLConnection.addRequestProperty("authorization", "Bearer " + this.spotifyText.text);
      }

      if (httpsURLConnection.getResponseCode() / 100 != 2) {
         if (flag) {
            this.spotifyText = this.getSpotifyText();
            return this.resolveObject(text, classValue, false);
         } else {
            return null;
         }
      } else {
         JsonReader jsonReader = new JsonReader(new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8));
         return (T)gson.fromJson(jsonReader, classValue);
      }
   }
}
