package org.phobos.core.media.mac;

import java.util.Objects;
import org.phobos.api.PlaybackListener;
import org.phobos.type.PLAYPAUSENEXTEnum;
import org.phobos.core.media.MediaPlayer;
import org.phobos.core.media.MediaTrack;

public class AppleScriptMediaPlayer extends MediaPlayer {
   public final AppleScriptMusic appleScriptMusic = new AppleScriptMusic();
   public long longValue2;
   public MediaTrack mediaTrack;
   public int intValue;
   public boolean flag = false;
   public boolean flag2;

   @Override
   public boolean isFlag22() {
      return this.flag;
   }

   @Override
   public boolean isFlag2() {
      return this.flag2;
   }

   @Override
   public void handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum pLAYPAUSENEXTEnum) {
      try {
         switch (pLAYPAUSENEXTEnum) {
            case NEXT:
               this.appleScriptMusic.run3();
               break;
            case PLAY_PAUSE:
               this.appleScriptMusic.run();
               break;
            case PREV:
               this.appleScriptMusic.run2();
         }
      } catch (Exception exception) {
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleException(exception));
         this.flag = false;
      }
   }

   @Override
   public int value() {
      if (!this.isFlag()) {
         throw new IllegalStateException("Position is not known yet");
      } else if (this.flag2) {
         long l = System.currentTimeMillis() - this.longValue2;
         return this.intValue + (int)l;
      } else {
         return this.intValue;
      }
   }

   @Override
   public void stop() {
      super.stop();
      this.flag = false;
   }

   public void handleInt2(int i) {
      if (i != this.intValue) {
         this.intValue = i;
         this.longValue2 = System.currentTimeMillis();
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleInt(i));
      }
   }

   @Override
   public void run2() throws Exception {
      String text3 = this.appleScriptMusic.describe3();
      if (!this.flag && !text3.isEmpty()) {
         this.flag = true;
         this.playbackListenerList.forEach(PlaybackListener::run2);
      }

      if (!Objects.equals(text3, this.mediaTrack == null ? null : this.mediaTrack.getText())) {
         String text = this.appleScriptMusic.describe2();
         String text2 = this.appleScriptMusic.describe();
         int i = this.appleScriptMusic.value();
         boolean flag = !this.check2();
         MediaTrack mediaTrack = new MediaTrack(text3, text, text2, i);
         this.mediaTrack = mediaTrack;
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleMediaTrack(mediaTrack));
         if (!flag) {
            this.handleInt2(0);
         }
      }

      boolean flag2 = this.appleScriptMusic.check();
      if (flag2 != this.flag2) {
         this.flag2 = flag2;
         this.playbackListenerList.forEach(playbackListener -> playbackListener.handleFlag(flag2));
      }

      int i2 = this.appleScriptMusic.value2();
      if (!this.isFlag() || Math.abs(i2 - this.value()) > 1000) {
         this.handleInt2(i2);
      }

      this.playbackListenerList.forEach(PlaybackListener::run);
   }

   @Override
   public boolean isFlag() {
      return this.intValue != -1;
   }

   @Override
   public MediaTrack getMediaTrack() {
      return this.mediaTrack;
   }

   public AppleScriptMediaPlayer() {
      this.intValue = -1;
   }
}
