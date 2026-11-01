package org.phobos.mixin.game.render.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.ShouldRenderEvent;
import org.phobos.module.render.NametagsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> implements EventSubscriber {
   @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
   private void shouldRenderHook(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      ShouldRenderEvent<T> event = this.invoke(new ShouldRenderEvent<>(entity));
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }
   }

   @Shadow
   protected abstract <E extends Entity> void renderLeash(T var1, float var2, MatrixStack var3, VertexConsumerProvider var4, E var5);

   @Shadow
   public abstract Identifier getTexture(T var1);

   @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
   private void renderLabelIfPresentHook(
      T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci
   ) {
      if (entity instanceof PlayerEntity && NametagsModule.check2()) {
         ci.cancel();
      }
   }

   @Shadow
   protected abstract void renderLabelIfPresent(T var1, Text var2, MatrixStack var3, VertexConsumerProvider var4, int var5, float var6);

   @Shadow
   public abstract void render(T var1, float var2, float var3, MatrixStack var4, VertexConsumerProvider var5, int var6);

   @Unique
   public <E extends Entity> void __renderLeash(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, E leashHolder) {
      this.renderLeash(entity, tickDelta, matrices, vertexConsumers, leashHolder);
   }
}
