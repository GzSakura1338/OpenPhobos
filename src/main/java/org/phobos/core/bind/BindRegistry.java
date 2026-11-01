package org.phobos.core.bind;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.phobos.core.input.Keys;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.core.system.OsDetector;
import org.phobos.core.util.collection.SortedNamedRegistry;

public class BindRegistry extends SortedNamedRegistry<BindKey> {
   public final Map<KeyboardMouseEnum, Map<Integer, BindKey>> keyboardMouseEnumMap = new EnumMap<>(KeyboardMouseEnum.class);

   public String describeKeyboardMouseEnum(KeyboardMouseEnum keyboardMouseEnum, String text, int i) {
      if (!OsDetector.getINSTANCE().check()) {
         return text;
      }

      String text2 = null;
      if (keyboardMouseEnum == KeyboardMouseEnum.Keyboard && i != -1 && i != 161) {
         text2 = GLFW.glfwGetKeyName(i, -1);
      }

      return text2 == null ? text : text2.toUpperCase();
   }

   public void run3() {
      try {
         for (Field field : Keys.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
               String text = field.getName();
               KeyboardMouseEnum keyboardMouseEnum = null;
               if (text.startsWith("KEY_")) {
                  text = text.substring("KEY_".length());
                  keyboardMouseEnum = KeyboardMouseEnum.Keyboard;
               } else if (text.startsWith("MOUSE_")) {
                  keyboardMouseEnum = KeyboardMouseEnum.Mouse;
               }

               if (keyboardMouseEnum != null) {
                  field.setAccessible(true);
                  int i = (Integer)field.get(null);
                  this.testNamed4(new BindKey(this.describeKeyboardMouseEnum(keyboardMouseEnum, text, i), keyboardMouseEnum, i));
               }
            }
         }
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }
   }

   @Nullable
   public BindKey resolveBindKey(KeyboardMouseEnum keyboardMouseEnum, int i) {
      return this.keyboardMouseEnumMap.getOrDefault(keyboardMouseEnum, Collections.emptyMap()).get(i);
   }

   public boolean testNamed2(BindKey bindKey) {
      if (super.testNamed2(bindKey)) {
         this.keyboardMouseEnumMap
            .computeIfAbsent(bindKey.getKeyboardMouseEnum(), keyboardMouseEnum -> new ConcurrentHashMap<>())
            .put(bindKey.getIntValue(), bindKey);
         return true;
      } else {
         return false;
      }
   }

   public boolean testNamed(BindKey bindKey) {
      Map<Integer, BindKey> map = this.keyboardMouseEnumMap.get(bindKey.getKeyboardMouseEnum());
      if (map != null) {
         map.remove(bindKey.getIntValue(), bindKey);
      }

      return super.testNamed(bindKey);
   }
}
