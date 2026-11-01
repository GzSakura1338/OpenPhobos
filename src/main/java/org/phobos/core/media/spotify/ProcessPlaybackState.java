package org.phobos.core.media.spotify;

import org.phobos.api.PlaybackState;

public class ProcessPlaybackState implements PlaybackState {
   public static final long longValue = 1000L;
   public final CooldownMap cooldownMap;
   public static final long longValue2 = 600000L;
   public int position;
   public boolean flag;
   public int intValue;
   public final ProcessMemoryScanner processMemoryScanner;

   public ProcessPlaybackState(ProcessMemoryScanner processMemoryScanner, long l) {
      this.processMemoryScanner = processMemoryScanner;
      this.cooldownMap = new CooldownMap(181772040L, l);
      this.cooldownMap.handleText("position", 181772944L);
      this.cooldownMap.handleText("length", 181772960L);
      this.cooldownMap.handleText("is_playing", 181773016L);
      this.check();
   }

   @Override
   public boolean check() {
      this.position = this.processMemoryScanner.computeLong(this.cooldownMap.computeText("position"));
      this.intValue = this.processMemoryScanner.computeLong(this.cooldownMap.computeText("length"));
      this.flag = this.processMemoryScanner.testLong6(this.cooldownMap.computeText("is_playing"));
      return this.alwaysTrue();
   }

   @Override
   public int getIntValue() {
      return this.intValue;
   }

   @Override
   public int getPosition() {
      return this.position;
   }

   @Override
   public boolean alwaysTrue() {
      return this.position <= this.intValue && this.position >= 0 && this.intValue <= 600000L && this.intValue >= 1000L;
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }
}
