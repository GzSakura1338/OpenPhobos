package org.phobos.core.media.linux;

import java.util.Objects;
import org.phobos.api.PlaybackListener;
import org.phobos.type.PLAYPAUSENEXTEnum;
import org.phobos.core.media.MediaPlayer;
import org.phobos.core.media.MediaTrack;

public class MprisMediaPlayer extends MediaPlayer {
   public boolean flag;
   public int intValue;
   public long longValue2;
   public boolean flag2 = false;
   public MediaTrack mediaTrack;
   public final MprisSpotifyClient mprisSpotifyClient;

   @Override
   public boolean isFlag22() {
      return this.flag2;
   }

   public void handleInt2(int i) {
      if (i != this.intValue) {
         this.intValue = i;
         this.longValue2 = System.currentTimeMillis();
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleInt(i));
      }
   }

   @Override
   public boolean isFlag2() {
      return this.flag;
   }

   @Override
   public void run2() throws Exception {
      String text3 = this.mprisSpotifyClient.describe3();
      if (!this.flag2 && !text3.isEmpty()) {
         this.flag2 = true;
         this.playbackListenerList.forEach(PlaybackListener::run2);
      }

      if (!Objects.equals(text3, this.mediaTrack == null ? null : this.mediaTrack.getText())) {
         String text = this.mprisSpotifyClient.describe2();
         String text2 = this.mprisSpotifyClient.describe();
         int i = this.mprisSpotifyClient.getInteger();
         boolean flag = !this.check2();
         MediaTrack mediaTrack = new MediaTrack(text3, text, text2, i);
         this.mediaTrack = mediaTrack;
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleMediaTrack(mediaTrack));
         if (!flag) {
            this.handleInt2(0);
         }
      }

      boolean flag2 = this.mprisSpotifyClient.check();
      if (flag2 != this.flag) {
         this.flag = flag2;
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleFlag(flag2));
      }

      int i2 = this.mprisSpotifyClient.getInteger2();
      if (!this.isFlag() || Math.abs(i2 - this.value()) >= 1000) {
         this.handleInt2(i2);
      }

      this.playbackListenerList.forEach(PlaybackListener::run);
   }

   @Override
   public MediaTrack getMediaTrack() {
      return this.mediaTrack;
   }

   @Override
   public int value() {
      if (!this.isFlag()) {
         throw new IllegalStateException("Position is not known yet");
      } else if (this.flag) {
         long l = System.currentTimeMillis() - this.longValue2;
         return this.intValue + (int)l;
      } else {
         return this.intValue;
      }
   }

   public MprisMediaPlayer() {
      this.intValue = -1;
      this.mprisSpotifyClient = new MprisSpotifyClient();
   }

   @Override
   public boolean isFlag() {
      return this.intValue != -1;
   }

   @Override
   public void handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum pLAYPAUSENEXTEnum) {
      try {
         switch (pLAYPAUSENEXTEnum) {
            case NEXT:
               this.mprisSpotifyClient.run();
               break;
            case PLAY_PAUSE:
               this.mprisSpotifyClient.run2();
               break;
            case PREV:
               this.mprisSpotifyClient.run4();
         }
      } catch (Exception exception) {
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleException(exception));
         this.flag2 = false;
      }
   }

   @Override
   public void stop() {
      super.stop();
      this.flag2 = false;
   }
}
