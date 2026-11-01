package org.phobos.core.bind;

import java.util.Iterator;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.KeyLookup;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.module.Module;
import org.phobos.type.KeyboardMouseEnum;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class BindManager extends Feature implements KeyLookup {
   public final BindRegistry bindRegistry;
   public long longValue;

   public BindRegistry getBindRegistry() {
      return this.bindRegistry;
   }

   @Override
   public BindKey resolveBindKey2(String text) {
      return this.bindRegistry.resolveOptional2(text).orElse(null);
   }

   @Override
   public BindKey resolveBindKey(KeyboardMouseEnum keyboardMouseEnum, int i) {
      return this.bindRegistry.resolveBindKey(keyboardMouseEnum, i);
   }

   @Override
   public boolean testKeyboardMouseEnum(KeyboardMouseEnum keyboardMouseEnum, int i) {
      if (this.longValue == 0L) {
         if (mc.getWindow() != null) {
            this.longValue = mc.getWindow().getHandle();
         }

         return false;
      } else {
         return switch (keyboardMouseEnum) {
            case Mouse -> GLFW.glfwGetMouseButton(this.longValue, i) == 1;
            case Keyboard -> GLFW.glfwGetKey(this.longValue, i) == 1;
         };
      }
   }

   @Override
   public Stream<BindKey> stream() {
      return this.bindRegistry.stream();
   }

   @NotNull
   @Override
   public Iterator<BindKey> iterator() {
      return this.bindRegistry.iterator();
   }

   public BindManager(PhobosClient phobosClient, long l) {
      this.longValue = l;
      this.bindRegistry = new BindRegistry();
      this.bindRegistry.run3();
      this.handleListener(
         new Listener<OnMouseButtonEvent>() {
            public void handleObject2(OnMouseButtonEvent onMouseButtonEvent) {
               if (onMouseButtonEvent.getIntValue() == 1 && MinecraftHolder.mc.currentScreen == null) {
                  BindKey bindKey = BindManager.this.resolveBindKey(
                     onMouseButtonEvent.getKeyboardMouseEnum(), onMouseButtonEvent.getIntValue2()
                  );
                  if (bindKey == null) {
                     return;
                  }

                  for (Module module : phobosClient.getModuleManager().getList()) {
                     Setting<KeyCombination> setting = module.getBind();
                     if (setting.getValue().getBindKeySet().contains(bindKey)) {
                        module.run9();
                     }
                  }

                  for (Macro macro : phobosClient.getMacroManager()) {
                     if (macro.getBindKey().equals(bindKey)) {
                        macro.run();
                     }
                  }
               }
            }
         }
      );
   }

   @Nullable
   public BindKey resolveBindKey3(String text) {
      for (BindKey bindKey : this.bindRegistry) {
         if (bindKey.describe3().startsWith(text)) {
            return bindKey;
         }
      }

      return null;
   }

   public long getLongValue() {
      return this.longValue;
   }
}
