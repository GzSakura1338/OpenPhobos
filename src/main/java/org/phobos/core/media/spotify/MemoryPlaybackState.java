package org.phobos.core.media.spotify;

import org.phobos.api.PlaybackState;

public class MemoryPlaybackState implements PlaybackState {
   public boolean flag;
   public final SpotifyMemoryReader spotifyMemoryReader;

   @Override
   public boolean check() {
      this.flag = this.spotifyMemoryReader.check2();
      return true;
   }

   public MemoryPlaybackState(SpotifyMemoryReader spotifyMemoryReader) {
      this.spotifyMemoryReader = spotifyMemoryReader;
   }

   @Override
   public int getIntValue() {
      return -1;
   }

   @Override
   public int getPosition() {
      return -1;
   }

   @Override
   public boolean alwaysTrue() {
      return true;
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }
}
