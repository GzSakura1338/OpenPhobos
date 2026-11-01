package org.phobos.core.hud;

public class StateFactory {
   public static ClockTime resolveClockTime(boolean flag) {
      ClockTime clockTime = new ClockTime();
      if (flag) {
         clockTime.run();
      }

      return clockTime;
   }
}
