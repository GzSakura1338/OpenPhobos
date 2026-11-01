package org.phobos.core.system;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.PhobosLog;

public class MemoryCleaner {
   public static final List<Object> objectList = new ArrayList<>();
   public static final Logger LOGGER = LoggerFactory.getLogger(MemoryCleaner.class);

   public static void handleObject(Object object) {
      objectList.add(object);
   }

   public static void handleText(String text) {
      PhobosLog.handleText("JavaVM", text);
   }

   public static void run() {
      handleText("Starting garbage collection session.");

      try {
         for (Object object : objectList) {
            Class<?> classValue = object.getClass();

            for (Field field : classValue.getDeclaredFields()) {
               field.setAccessible(true);
               if (field.getType().equals(List.class)) {
                  List<?> list = (List<?>)field.get(object);

                  try {
                     list.clear();
                     handleText("Cleared %s as List.class".formatted(field.getName()));
                  } catch (Throwable var19) {
                  }
               } else if (field.getType().equals(Map.class)) {
                  Map<?, ?> map = (Map<?, ?>)field.get(object);

                  try {
                     map.clear();
                     handleText("Cleared %s as Map.class".formatted(field.getName()));
                  } catch (Throwable var18) {
                  }
               } else if (field.getType().equals(Queue.class)) {
                  Queue<?> queue = (Queue<?>)field.get(object);

                  try {
                     queue.clear();
                     handleText("Cleared %s as Queue.class".formatted(field.getName()));
                  } catch (Throwable var17) {
                  }
               } else if (field.getType().equals(Collection.class)) {
                  Collection<?> collection = (Collection<?>)field.get(object);

                  try {
                     collection.clear();
                     handleText("Cleared %s as Collection.class".formatted(field.getName()));
                  } catch (Throwable var16) {
                  }
               }
            }
         }
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      } finally {
         System.gc();
         handleText("Closing memory section");
      }
   }
}
