package org.phobos.core.media;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.phobos.api.PlaybackListener;
import org.phobos.api.MediaPlayerApi;
import org.phobos.core.media.spotify.SpotifyApiClient;

public abstract class MediaPlayer implements MediaPlayerApi {
   public SpotifyApiClient spotifyApiClient;
   public PlaybackRequest playbackRequest;
   public long longValue;
   public final List<PlaybackListener> playbackListenerList = new ArrayList<>();
   public ScheduledFuture<?> scheduledFuture;

   @Override
   public void handlePlaybackListener(PlaybackListener playbackListener) {
      this.playbackListenerList.remove(playbackListener);
   }

   public MediaPlayer() {
      this.longValue = -1L;
   }

   public void run() {
      try {
         long l = System.currentTimeMillis() - this.longValue;
         if (l < this.playbackRequest.getLongValue()) {
            return;
         }

         this.run2();
      } catch (Exception exception) {
         this.longValue = System.currentTimeMillis();
         this.stop();
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleException(exception));
         if (this.playbackRequest.isFlag()) {
            this.resolveMediaPlayerApi(this.playbackRequest);
         }
      }
   }

   @Override
   public void stop() {
      synchronized (this) {
         if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
         }
      }
   }

   @Override
   public boolean check() {
      return this.scheduledFuture != null;
   }

   @Override
   public void handlePlaybackListener2(PlaybackListener playbackListener) {
      this.playbackListenerList.add(playbackListener);
   }

   @Override
   public final SpotifyApiClient getSpotifyApiClient() {
      if (this.spotifyApiClient == null) {
         this.spotifyApiClient = new SpotifyApiClient();
      }

      return this.spotifyApiClient;
   }

   @Override
   public PlaybackRequest getPlaybackRequest() {
      return this.playbackRequest;
   }

   @Override
   public MediaPlayerApi resolveMediaPlayerApi(PlaybackRequest playbackRequest) {
      synchronized (this) {
         this.playbackRequest = playbackRequest;
         if (this.check()) {
            throw new IllegalStateException("The SpotifyAPI is already initialized");
         }

         this.scheduledFuture = Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(this::run, 0L, 1L, TimeUnit.SECONDS);
         return this;
      }
   }

   public abstract void run2() throws Exception;
}
