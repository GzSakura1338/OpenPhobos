package org.phobos.mixin.game.render.internal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Predicate;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.earthhack.mixin.EventSubscriber;
import org.joml.Matrix4f;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.render.shader.ShaderManager;
import org.phobos.core.ClientHolder;
import org.phobos.core.hud.FpsCounter;
import org.phobos.core.render.Render3d;
import org.phobos.event.FindCrosshairTargetEvent;
import org.phobos.event.GetFovEvent;
import org.phobos.event.RenderEvent;
import org.phobos.event.RenderTransformedWorldEvent;
import org.phobos.module.player.BlockTweaksModule;
import org.phobos.module.render.NoBobModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.RescaleModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements MinecraftHolder, EventSubscriber {
   @Shadow
   private float lastFovMultiplier;
   @Shadow
   private float viewDistance;
   @Shadow
   private boolean renderingPanorama;
   @Shadow
   private float fovMultiplier;
   @Shadow
   @Final
   MinecraftClient client;

   @ModifyExpressionValue(
      method = "findCrosshairTarget",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;")
   )
   private HitResult findCrosshairTargetHook(HitResult result, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
      if (camera != this.client.player) {
         return result;
      }

      FindCrosshairTargetEvent event = this.invoke(new FindCrosshairTargetEvent(result, camera, blockInteractionRange, entityInteractionRange, tickDelta));
      return event.getHitResult();
   }

   @Inject(method = "loadPrograms", at = @At("RETURN"))
   private void loadShaderPrograms(ResourceFactory factory, CallbackInfo ci) {
      ShaderManager.INSTANCE.handleResourceFactory(factory);
   }

   @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
   private void getFovHook(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
      GetFovEvent event = this.invoke(new GetFovEvent((Double)cir.getReturnValue()));
      cir.setReturnValue(event.getDoubleValue());
   }

   @Shadow
   public abstract void render(RenderTickCounter var1, boolean var2);

   @Redirect(
      method = "findCrosshairTarget",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
      )
   )
   private EntityHitResult findCrosshairTargetHook(Entity camera, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double maxDistance) {
      return ProjectileUtil.raycast(
         camera,
         min,
         max,
         box,
         entity -> !BlockTweaksModule.check() && !entity.isSpectator() && entity.canHit() && !BlockTweaksModule.testEntity(entity),
         maxDistance
      );
   }

   @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
   private void bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      this.invoke(new NoRenderModule.Bob(), ci);
      if (!ci.isCancelled()) {
         NoBobModule.handleMatrixStack(matrices, tickDelta, ci);
      }
   }

   @Inject(
      method = "renderWorld",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V",
         shift = Shift.AFTER
      )
   )
   private void postRenderWorld(RenderTickCounter renderTickCounter, CallbackInfo ci) {
      if (mc.world != null && mc.player != null) {
         ClientHolder.getPhobosClient().getShaderEffectManager().run3();
      }
   }

   @Redirect(
      method = "renderHand",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D")
   )
   private double handGetFovHook(GameRenderer renderer, Camera camera, float tickDelta, boolean changingFov) {
      if (this.renderingPanorama) {
         return 90.0;
      }

      double d = 70.0;
      if (changingFov) {
         d = ((Integer)(Object)this.client.options.getFov().getValue()).intValue();
         d *= MathHelper.lerp(tickDelta, this.lastFovMultiplier, this.fovMultiplier);
      }

      if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).isDead()) {
         float f = Math.min(((LivingEntity)camera.getFocusedEntity()).deathTime + tickDelta, 20.0F);
         d /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
      }

      CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
      if (cameraSubmersionType == CameraSubmersionType.LAVA || cameraSubmersionType == CameraSubmersionType.WATER) {
         d *= MathHelper.lerp((Double)(Object)this.client.options.getFovEffectScale().getValue(), 1.0, 0.85714287F);
      }

      return d;
   }

   @Inject(method = "getBasicProjectionMatrix", at = @At("TAIL"), cancellable = true)
   public void matrixHook(double d, CallbackInfoReturnable<Matrix4f> cir) {
      MatrixStack stack = new MatrixStack();
      stack.peek().getPositionMatrix().identity();
      stack.peek()
         .getPositionMatrix()
         .mul(new Matrix4f().setPerspective((float)(d * (float) (Math.PI / 180.0)), RescaleModule.value(), 0.05F, this.viewDistance * 4.0F));
      cir.setReturnValue(stack.peek().getPositionMatrix());
   }

   @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
   private void tiltViewWhenHurt(CallbackInfo ci) {
      this.invoke(new NoRenderModule.Hurt(), ci);
   }

   @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
   private void showFloatingItem(ItemStack floatingItem, CallbackInfo ci) {
      if (floatingItem != null && floatingItem.isOf(Items.TOTEM_OF_UNDYING)) {
         this.invoke(new NoRenderModule.Totems(), ci);
      }
   }

   @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0))
   private void renderTransformedWorldHook(RenderTickCounter renderTickCounter, CallbackInfo ci) {
      mc.getProfiler().push("PhobosLite-Game");
      Camera camera = mc.gameRenderer.getCamera();
      MatrixStack matrixStack = new MatrixStack();
      RenderSystem.getModelViewStack().pushMatrix().mul(matrixStack.peek().getPositionMatrix());
      matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
      matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
      RenderSystem.applyModelViewMatrix();
      Render3d.matrix4f3.set(RenderSystem.getProjectionMatrix());
      Render3d.matrix4f2.set(RenderSystem.getModelViewMatrix());
      Render3d.matrix4f.set(matrixStack.peek().getPositionMatrix());
      FpsCounter.run();
      this.invoke(new RenderTransformedWorldEvent(matrixStack, renderTickCounter.getTickDelta(false)));
      RenderSystem.getModelViewStack().popMatrix();
      RenderSystem.applyModelViewMatrix();
      mc.getProfiler().pop();
   }

   @Inject(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = Shift.AFTER)
   )
   private void renderHook0(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
      this.invoke(new RenderEvent(tickCounter.getTickDelta(tick)));
   }
}
