package org.phobos.api;

import java.util.concurrent.CompletableFuture;
import org.phobos.core.media.MediaTrack;
import org.phobos.core.media.PlaybackRequest;
import org.phobos.core.media.PlaybackRequest;
import org.phobos.core.media.spotify.SpotifyApiClient;
import org.phobos.type.PLAYPAUSENEXTEnum;

public interface MediaPlayerApi {
   boolean check();

   void handlePlaybackListener(PlaybackListener var1);

   boolean isFlag();

   default CompletableFuture<MediaPlayerApi> resolveCompletableFuture(PlaybackRequest playbackRequest) {
      return CompletableFuture.supplyAsync(() -> this.resolveMediaPlayerApi(playbackRequest));
   }

   boolean isFlag2();

   boolean isFlag22();

   PlaybackRequest getPlaybackRequest();

   SpotifyApiClient getSpotifyApiClient();

   default CompletableFuture<MediaPlayerApi> getCompletableFuture() {
      return CompletableFuture.supplyAsync(this::getMediaPlayerApi);
   }

   void stop();

   default MediaPlayerApi getMediaPlayerApi() {
      return this.resolveMediaPlayerApi(new PlaybackRequest.Builder().createPlaybackRequest());
   }

   default boolean check2() {
      return this.getMediaTrack() != null;
   }

   MediaPlayerApi resolveMediaPlayerApi(PlaybackRequest var1);

   void handlePlaybackListener2(PlaybackListener var1);

   int value();

   void handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum var1);

   MediaTrack getMediaTrack();
}
