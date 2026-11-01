package org.phobos.mixin.game.render.block;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.core.util.OneShotFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class MixinChestBlockEntityRenderer {
   @Inject(
      method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;FII)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void render(
      MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay, CallbackInfo ci
   ) {
      if (OneShotFlag.check()) {
         ci.cancel();
         lid.pitch = -(openFactor * (float) (Math.PI / 2));
         latch.pitch = lid.pitch;
         base.render(matrices, vertices, light, overlay);
         lid.render(matrices, vertices, light, overlay);
         latch.render(matrices, vertices, light, overlay);
      }
   }
}
