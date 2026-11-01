package org.phobos.core.util;

public class OneShotFlag {
   public static boolean flag;

   public static boolean check() {
      if (flag) {
         flag = false;
         return true;
      } else {
         return false;
      }
   }

   public static void run() {
      flag = true;
   }
}
