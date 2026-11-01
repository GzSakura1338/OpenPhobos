package org.phobos.core;

import java.util.function.Consumer;

public class ClientGuard {
   public static String text = "127.0.0.1";

   public static void handleText(String text) {
      System.err.println("[Phobos] #ERROR " + text);
   }

   public static void handleConsumer(Consumer<String> consumer) {
      consumer.accept("Offline mode");
   }

   public static void stop() {
      Runtime.getRuntime().halt(-1);
   }
}
