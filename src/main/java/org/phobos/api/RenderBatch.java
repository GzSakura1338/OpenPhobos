package org.phobos.api;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import org.phobos.core.render.Render3d;
import org.phobos.type.DefaultFluidEnum;

public interface RenderBatch {
   default void handleColor(Color color) {
      Render3d.handleColor(color);
   }

   void run4();

   void run2();

   static RenderBatch getRenderBatch() {
      return DefaultFluidEnum.Default.getRenderer();
   }

   void run3();

   default void run5() {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   void run();
}
