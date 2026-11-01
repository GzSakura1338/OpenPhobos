package org.phobos.core.input;

import lombok.SneakyThrows;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.phobos.core.input.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingCounter {
   public static final Logger LOGGER = LoggerFactory.getLogger(SettingCounter.class);
   public final Map<String, Integer> textMap = new HashMap<>();

   public Map<String, Integer> getTextMap() {
      return this.textMap;
   }

   @SneakyThrows
   public void run() throws IllegalAccessException {
      for (Field field : Keys.class.getDeclaredFields()) {
         if (Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
            String text = field.getName();
            if (text.startsWith("KEY_")) {
               text = text.substring("KEY_".length());
            }

            int i = (Integer)field.get(null);
            this.textMap.put(text, i);
         }
      }
   }
}
