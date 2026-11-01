package org.phobos.api;

public interface PlaybackState {
   boolean isFlag();

   int getIntValue();

   boolean alwaysTrue();

   default boolean check2() {
      return this.getPosition() >= 0 && (!this.check3() || this.getPosition() <= this.getIntValue());
   }

   int getPosition();

   default boolean check3() {
      return this.getIntValue() > 0;
   }

   boolean check();
}
