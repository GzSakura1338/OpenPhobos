package org.phobos.core.hud;

import java.util.ArrayList;

public class FpsCounter {
   public static final ArrayList<Long> longValueArrayList = new ArrayList<>(0);

   public static void run() {
      longValueArrayList.add(System.currentTimeMillis());
   }

   public static int value() {
      run2();
      return longValueArrayList.size();
   }

   public static void run2() {
      longValueArrayList.removeIf(longValue -> longValue + 1000L < System.currentTimeMillis());
   }
}
