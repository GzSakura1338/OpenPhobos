package org.phobos.mixin.game.render.player;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.api.CloakAccess;
import org.phobos.module.client.CapesModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class MixinPlayerEntityModel implements CloakAccess {
   @Shadow
   @Final
   public ModelPart cloak;

   @Override
   public ModelPart getCloak() {
      return this.cloak;
   }

   @Inject(method = "renderCape", at = @At("HEAD"), cancellable = true)
   private void renderCapeHook(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, CallbackInfo ci) {
      if (CapesModule.check()) {
         ci.cancel();
      }
   }
}
