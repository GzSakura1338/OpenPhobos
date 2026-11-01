package org.phobos.type;

public enum MorningAfternoonEnum {
   Morning,
   Afternoon,
   Evening,
   Night;

   public static MorningAfternoonEnum of(int hour) {
      if (hour > 21 || hour <= 4) {
         return Night;
      } else if (hour < 12) {
         return Morning;
      } else {
         return hour <= 17 ? Afternoon : Evening;
      }
   }
}
