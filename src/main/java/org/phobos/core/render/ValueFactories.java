package org.phobos.core.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.util.Window;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.util.StagedPipeline;

public final class ValueFactories implements MinecraftHolder {
   public static final StagedPipeline<Double> doubleValueStagedPipeline = StagedPipeline.createStagedPipeline2(
      (propertyBag, doubleValue) -> {
         Window window = mc.getWindow();
         Matrix4f matrix4f = new Matrix4f()
            .setOrtho(0.0F, (float)(window.getFramebufferWidth() / doubleValue), (float)(window.getFramebufferHeight() / doubleValue), 0.0F, 1000.0F, 21000.0F);
         RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_Z);
         Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
         matrix4fStack.popMatrix();
         matrix4fStack.pushMatrix();
         matrix4fStack.translation(0.0F, 0.0F, -11000.0F);
         RenderSystem.applyModelViewMatrix();
      },
      (propertyBag, doubleValue) -> RenderSystem.applyModelViewMatrix()
   );

   public static StagedPipeline<Double> getDoubleValueStagedPipeline() {
      return doubleValueStagedPipeline;
   }

   public ValueFactories() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
