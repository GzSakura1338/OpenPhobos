package org.phobos.core.media.spotify;

import java.util.Objects;
import org.phobos.api.PlaybackListener;
import org.phobos.api.PlaybackState;
import org.phobos.type.PLAYPAUSENEXTEnum;
import org.phobos.core.media.MediaPlayer;
import org.phobos.core.media.MediaTrack;
import org.phobos.core.media.UnknownTrack;

public class SpotifyMediaPlayer extends MediaPlayer {
   public boolean flag;
   public SpotifyMemoryReader spotifyMemoryReader;
   public boolean flag2;
   public long longValue2;
   public int intValue = -1;
   public long longValue3;
   public MediaTrack mediaTrack;
   public long longValue4;

   @Override
   public boolean isFlag22() {
      return this.spotifyMemoryReader != null && this.spotifyMemoryReader.check();
   }

   @Override
   public void handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum pLAYPAUSENEXTEnum) {
      if (!this.isFlag22()) {
         throw new IllegalStateException("Spotify is not connected");
      }

      switch (pLAYPAUSENEXTEnum) {
         case PLAY_PAUSE:
            this.spotifyMemoryReader.handleInt(176);
            break;
         case PREV:
            this.spotifyMemoryReader.handleInt(177);
            break;
         case NEXT:
            this.spotifyMemoryReader.handleInt(179);
      }

      this.run();
   }

   public SpotifyMediaPlayer() {
      this.flag = false;
      this.longValue4 = -1L;
   }

   @Override
   public void run2() throws Exception {
      if (!this.isFlag22()) {
         this.spotifyMemoryReader = new SpotifyMemoryReader();
         this.playbackListenerList.forEach(PlaybackListener::run2);
      }

      PlaybackState playbackState = this.spotifyMemoryReader.getPlaybackState();
      String text = this.spotifyMemoryReader.describe2();
      if (this.spotifyMemoryReader.testText(text) && playbackState.check()) {
         if (!Objects.equals(text, this.mediaTrack == null ? null : this.mediaTrack.getText())) {
            if ((!this.check2() || playbackState.getIntValue() == this.mediaTrack.getIntValue())
               && playbackState.getPosition() >= this.intValue
               && System.currentTimeMillis() - this.longValue3 < 2000L) {
               return;
            }

            UnknownTrack unknownTrack = this.spotifyMemoryReader.getUnknownTrack();
            if (unknownTrack != UnknownTrack.INSTANCE) {
               int i = playbackState.getIntValue();
               boolean flag = !this.check2();
               if (!playbackState.check3()) {
                  try {
                     SpotifyTrack spotifyTrack = this.getSpotifyApiClient().resolveSpotifyTrack2(text);
                     if (spotifyTrack != null) {
                        i = spotifyTrack.integer2;
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }
               }

               MediaTrack mediaTrack = new MediaTrack(text, unknownTrack.getName(), unknownTrack.getText(), i);
               this.mediaTrack = mediaTrack;
               this.playbackListenerList.forEach(playbackListener -> playbackListener.handleMediaTrack(mediaTrack));
               if (!flag) {
                  this.handleInt2(0, true);
               }
            }
         }

         boolean flag2 = playbackState.isFlag();
         if (flag2 != this.flag2) {
            this.flag2 = flag2;
            if (!playbackState.check2()) {
               this.flag = false;
               this.intValue = -1;
            }

            this.playbackListenerList.forEach(playbackListener -> playbackListener.handleFlag(flag2));
         }

         int i2 = playbackState.getPosition();
         if (playbackState.check2() && i2 != this.longValue4) {
            this.longValue4 = i2;
            this.handleInt2(i2, false);
         }

         this.playbackListenerList.forEach(PlaybackListener::run);
         this.longValue3 = System.currentTimeMillis();
      } else {
         this.flag = false;
         this.intValue = -1;
         throw new IllegalStateException("Could not update playback");
      }
   }

   @Override
   public MediaTrack getMediaTrack() {
      return this.mediaTrack;
   }

   @Override
   public void stop() {
      super.stop();
      if (this.spotifyMemoryReader != null) {
         this.spotifyMemoryReader.close();
         this.spotifyMemoryReader = null;
      }
   }

   @Override
   public boolean isFlag2() {
      return this.flag2;
   }

   public void handleInt2(int i, boolean flag) {
      if (i != this.intValue || flag) {
         this.flag = flag || this.intValue != -1 || !this.flag2;
         this.intValue = i;
         this.longValue2 = System.currentTimeMillis();
         if (this.flag) {
            this.playbackListenerList.forEach(playbackListener -> playbackListener.handleInt(i));
         }
      }
   }

   @Override
   public int value() {
      if (!this.flag) {
         throw new IllegalStateException("Position is not known yet. Pause the song for a second and try again.");
      } else if (this.flag2) {
         long l = System.currentTimeMillis() - this.longValue2;
         long l2 = this.intValue + l;
         return this.check2() ? (int)Math.min(l2, this.mediaTrack.getIntValue()) : (int)l2;
      } else {
         return this.intValue;
      }
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }
}
