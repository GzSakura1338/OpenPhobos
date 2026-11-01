package org.phobos.api;

import java.awt.Color;
import org.phobos.core.util.ErrorRegistry;
import org.phobos.core.render.shader.ShaderEffectManager;

public interface UniformSetup {
   static <T extends Object & ColoredUniformOwner> void handleObject(T object, Color color, Color color2) {
      handleObject2(object);
      object.handleColor(color, color2);
      object.run3();
   }

   static <T extends Object & UniformOwner> void handleObject2(T object) {
      object.getFloatUniform1().handleFloat5(ShaderEffectManager.floatValue);
   }

   static NamedResolver createErrorRegistry() {
      return new ErrorRegistry();
   }
}
