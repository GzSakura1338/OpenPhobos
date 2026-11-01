package org.phobos.core.util;

public class StackTraceUtils {
   public static StackTraceInfo createStackTraceInfo() {
      return new StackTraceInfo(Thread.currentThread().getStackTrace());
   }
}
