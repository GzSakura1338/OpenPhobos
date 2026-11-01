package org.phobos.core.system;

import java.util.Map;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.phobos.data.NameRecord2;
import org.phobos.core.PhobosLog;
import org.phobos.core.tick.TimeUtils;

public final class Profiler {
   public static final Map<String, Long> textMap = new ConcurrentHashMap<>();
   public static final Thread thread = new Thread(() -> {
      try {
         run();
         Thread.sleep(150L);
      } catch (Throwable var1) {
      }
   });
   public static final Map<NameRecord2, Long> nameRecord2Map = new ConcurrentHashMap<>();

   public static void handleText(String text) {
      Long longValue = textMap.remove(text);
      if (longValue != null) {
         long l = TimeUtils.value();
         nameRecord2Map.put(new NameRecord2(text, l - longValue), l);
      }
   }

   public static void run() {
      for (Map.Entry<NameRecord2, Long> entry : nameRecord2Map.entrySet()) {
         NameRecord2 nameRecord2 = entry.getKey();
         if (entry.getValue() + 1000L < TimeUtils.value()) {
            nameRecord2Map.remove(nameRecord2);
         } else if (nameRecord2.millis() > 2000L) {
            PhobosLog.handleText(
               "Profiler",
               "Task %s executed in %s (If you are seeing this, this means some kind of overload has happened, please report to the developers)"
                  .formatted(nameRecord2.name(), nameRecord2.millis())
            );
         }
      }
   }

   public Profiler() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleText2(String text) {
      textMap.put(text, TimeUtils.value());
   }

   static {
      thread.start();
   }

   public static Stream<NameRecord2> stream() {
      return nameRecord2Map.keySet().stream();
   }
}
