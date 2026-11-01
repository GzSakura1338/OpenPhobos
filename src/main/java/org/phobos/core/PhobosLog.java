package org.phobos.core;

import org.phobos.core.user.UserSession;

public class PhobosLog {
   public static boolean flag;

   public static void handleText(String text, String text2) {
      System.out.println("[Phobos] #INFO [%s] ".formatted(text) + text2);
   }

   public static void handleText2(String text) {
      if (UserSession.flag2) {
         System.out.println(text);
      }
   }

   public static PhobosLog.Counter createCounter(String text, int i) {
      return new PhobosLog.Counter(text, i);
   }

   public static class Counter {
      public final int size;
      public final String name;
      public int intValue = 1;

      public int getSize() {
         return this.size;
      }

      public void run() {
         PhobosLog.handleText("Loader", this.name + " [%s/%s]".formatted(this.intValue++, this.size));
      }

      public String getName() {
         return this.name;
      }

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public Counter(String text, int i) {
         this.name = text;
         this.size = i;
      }

      public void run2() {
         PhobosLog.handleText("Loader", this.name + " [%s/%s]".formatted(this.intValue, this.size));
      }

      public int getIntValue() {
         return this.intValue;
      }
   }
}
