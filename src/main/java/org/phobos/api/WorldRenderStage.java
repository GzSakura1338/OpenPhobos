package org.phobos.api;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.phobos.mixin.game.render.internal.IWorldRenderer;

public interface WorldRenderStage {
   default int value() {
      return 0;
   }

   boolean check();

   void handleIWorldRenderer(IWorldRenderer var1, MatrixStack var2, float var3, Camera var4, VertexConsumerProvider var5);

   void handleClientWorld(ClientWorld var1);
}
