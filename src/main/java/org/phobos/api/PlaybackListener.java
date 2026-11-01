package org.phobos.api;

import org.phobos.core.media.MediaTrack;

public interface PlaybackListener {
   void handleInt(int var1);

   void run();

   void handleFlag(boolean var1);

   void handleException(Exception var1);

   void handleMediaTrack(MediaTrack var1);

   void run2();
}
