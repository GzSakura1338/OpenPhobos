package org.phobos.mixin.game.render.entity;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldView;
import org.earthhack.ducks.IEntityRenderDispatcher;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Quaternionf;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.ShaderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher implements EventSubscriber, IEntityRenderDispatcher, MinecraftHolder {
   @Shadow
   public Camera camera;

   @Shadow
   public abstract <T extends Entity> EntityRenderer<? super T> getRenderer(T var1);

   @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
   private void renderFireHook(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation, CallbackInfo ci) {
      if (ShaderModule.testEntity2(entity)) {
         ci.cancel();
      }
   }

   @Override
   public double renderPosY() {
      return this.camera.getPos().y;
   }

   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   public <E extends Entity> void render(
      E entity,
      double x,
      double y,
      double z,
      float yaw,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (entity == null) {
         ci.cancel();
      }
   }

   @Override
   public double renderPosX() {
      return this.camera.getPos().x;
   }

   @Override
   public double renderPosZ() {
      return this.camera.getPos().z;
   }

   @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
   private static void renderShadowHook(
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      Entity entity,
      float opacity,
      float tickDelta,
      WorldView world,
      float radius,
      CallbackInfo ci
   ) {
      if (NoRenderModule.check()) {
         ci.cancel();
      }
   }
}
