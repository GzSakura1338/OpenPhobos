package org.phobos.core.inventory;

public final class SwitchOverride {
   public static volatile boolean flag = false;
   public static int intValue = -1;
   public static int intValue2 = -1;
   public static int intValue3 = -1;

   public static void run() {
      intValue = -1;
   }

   public static boolean testFlag(boolean flag) {
      return switch (intValue3) {
         case -1 -> flag;
         case 0 -> false;
         case 1 -> true;
         default -> throw new IllegalStateException("Invalid override index: %s".formatted(intValue3));
      };
   }

   public static boolean testFlag2(boolean flag) {
      return switch (intValue2) {
         case -1 -> flag;
         case 0 -> false;
         case 1 -> true;
         default -> throw new IllegalStateException("Invalid override index: %s".formatted(intValue2));
      };
   }

   public static void handleFlag(boolean flag) {
      intValue2 = flag ? 1 : 0;
   }

   public static void handleFlag2(boolean flag) {
      intValue = flag ? 1 : 0;
   }

   public static void run2() {
      intValue2 = -1;
   }

   public static void handleRunnable(Runnable runnable) {
      flag = true;
      runnable.run();
      flag = false;
   }

   public static void handleFlag3(boolean flag) {
      intValue2 = flag ? 1 : 0;
   }

   public static void run3() {
      intValue2 = -1;
   }

   public SwitchOverride() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testFlag3(boolean flag) {
      return switch (intValue) {
         case -1 -> flag;
         case 0 -> false;
         case 1 -> true;
         default -> throw new IllegalStateException("Invalid override index: %s".formatted(intValue));
      };
   }
}
