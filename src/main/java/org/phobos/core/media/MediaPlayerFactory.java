package org.phobos.core.media;

import java.util.concurrent.CompletableFuture;
import org.phobos.api.MediaPlayerApi;
import org.phobos.core.media.spotify.SpotifyMediaPlayer;

public class MediaPlayerFactory {
   public static MediaPlayerApi resolveMediaPlayerApi(PlaybackRequest playbackRequest) {
      return createSpotifyMediaPlayer().resolveMediaPlayerApi(playbackRequest);
   }

   public static MediaPlayerApi createSpotifyMediaPlayer() {
      return new SpotifyMediaPlayer();
   }

   public static CompletableFuture<MediaPlayerApi> getCompletableFuture() {
      return CompletableFuture.supplyAsync(() -> createSpotifyMediaPlayer().getMediaPlayerApi());
   }

   public static CompletableFuture<MediaPlayerApi> resolveCompletableFuture(PlaybackRequest playbackRequest) {
      return CompletableFuture.supplyAsync(() -> resolveMediaPlayerApi(playbackRequest));
   }
}
