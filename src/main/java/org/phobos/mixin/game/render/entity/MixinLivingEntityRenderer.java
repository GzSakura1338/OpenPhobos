package org.phobos.mixin.game.render.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.render.PlayerRenderState;
import org.phobos.core.render.EntityRenderLayers;
import org.phobos.core.render.ColorUtils;
import org.phobos.event.RenderEvent3;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.ChamsModule;
import org.phobos.module.client.GameTiersModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>>
   extends MixinEntityRenderer<T>
   implements EventSubscriber,
   MinecraftHolder {
   @Shadow
   protected M model;
   @Shadow
   @Final
   protected List<FeatureRenderer<T, M>> features;

   @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
   private void getRenderLayerHook(T entity, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<RenderLayer> cir) {
      if (ChamsModule.testLivingEntity(entity) && !translucent && showBody) {
         cir.setReturnValue(EntityRenderLayers.resolveRenderLayer(this.getTexture(entity)));
      }
   }

   @Shadow
   protected abstract float getHandSwingProgress(T var1, float var2);

   @Shadow
   protected abstract float getAnimationProgress(T var1, float var2);

   @Shadow
   protected abstract boolean isShaking(T var1);

   @Shadow
   public static int getOverlay(LivingEntity entity, float whiteOverlayProgress) {
      return 0;
   }

   @Shadow
   protected abstract boolean isVisible(T var1);

   @Shadow
   protected abstract void scale(T var1, MatrixStack var2, float var3);

   @Shadow
   private static float getYaw(Direction direction) {
      return 0.0F;
   }

   @Shadow
   protected abstract float getAnimationCounter(T var1, float var2);

   @Inject(
      method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void renderHook(
      T livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci
   ) {
      if (!ChamsModule.flag) {
         ci.cancel();
         RenderEvent3<T, M> event = new RenderEvent3<>(livingEntity, f, tickDelta, matrixStack, i, this.model, this.features);
         ChamsModule.handleRenderEvent32((RenderEvent3<LivingEntity, EntityModel<LivingEntity>>)(Object)event);
         if (!event.isCancelled()) {
            float jitter = AnimationsModule.computeFloat3(tickDelta);
            boolean limbs = AnimationsModule.check6();
            boolean rotations = AnimationsModule.check4();
            matrixStack.push();
            this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, tickDelta);
            this.model.riding = livingEntity.hasVehicle();
            this.model.child = livingEntity.isBaby();
            float bodyYaw = MathHelper.lerpAngleDegrees(rotations ? jitter : tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            float headYaw = MathHelper.lerpAngleDegrees(rotations ? jitter : tickDelta, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float bodyOffset = headYaw - bodyYaw;
            if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
               bodyYaw = MathHelper.lerpAngleDegrees(rotations ? jitter : tickDelta, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
               bodyOffset = headYaw - bodyYaw;
               float bodyDelta = MathHelper.wrapDegrees(bodyOffset);
               if (bodyDelta < -85.0F) {
                  bodyDelta = -85.0F;
               }

               if (bodyDelta >= 85.0F) {
                  bodyDelta = 85.0F;
               }

               bodyYaw = headYaw - bodyDelta;
               if (bodyDelta * bodyDelta > 2500.0F) {
                  bodyYaw += bodyDelta * 0.2F;
               }

               bodyOffset = headYaw - bodyYaw;
            }

            float pitch = MathHelper.lerp(rotations ? jitter : tickDelta, livingEntity.prevPitch, livingEntity.getPitch());
            if (shouldFlipUpsideDown(livingEntity)) {
               pitch *= -1.0F;
               bodyOffset *= -1.0F;
            }

            bodyOffset = MathHelper.wrapDegrees(bodyOffset);
            if (livingEntity.isInPose(EntityPose.SLEEPING)) {
               Direction direction = livingEntity.getSleepingDirection();
               if (direction != null) {
                  float animationProgress = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                  matrixStack.translate(-direction.getOffsetX() * animationProgress, 0.0F, -direction.getOffsetZ() * animationProgress);
               }
            }

            float bodyDelta = livingEntity.getScale();
            matrixStack.scale(bodyDelta, bodyDelta, bodyDelta);
            float animationProgress = this.getAnimationProgress(livingEntity, tickDelta);
            this.setupTransforms(livingEntity, matrixStack, animationProgress, bodyYaw, tickDelta, bodyDelta);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(livingEntity, matrixStack, tickDelta);
            matrixStack.translate(0.0F, -1.501F, 0.0F);
            float limbSpeed = 0.0F;
            float limbPos = 0.0F;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
               limbSpeed = livingEntity.limbAnimator.getSpeed(limbs ? jitter : tickDelta);
               limbPos = livingEntity.limbAnimator.getPos(limbs ? jitter : tickDelta);
               if (livingEntity.isBaby()) {
                  limbPos *= 3.0F;
               }

               if (limbSpeed > 1.0F) {
                  limbSpeed = 1.0F;
               }
            }

            this.model.animateModel(livingEntity, limbPos, limbSpeed, tickDelta);
            this.model.setAngles(livingEntity, limbPos, limbSpeed, animationProgress, bodyOffset, pitch);
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            boolean visible = this.isVisible(livingEntity);
            boolean canSee = !visible && !livingEntity.isInvisibleTo(minecraftClient.player);
            boolean outline = minecraftClient.hasOutline(livingEntity);
            RenderLayer renderLayer = this.getRenderLayer(livingEntity, visible, canSee, outline);
            if (renderLayer != null) {
               VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
               int overlay = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, tickDelta));
               PlayerRenderState ghost = ((ILivingEntity)(Object)livingEntity).getGhostModel();
               this.model
                  .render(
                     matrixStack,
                     vertexConsumer,
                     i,
                     overlay,
                     ghost != null
                        ? ColorUtils.createColor6(Color.WHITE, ghost.getFloatValue2()).hashCode()
                        : (!canSee && livingEntity instanceof PlayerEntity ? ChamsModule.getColor().hashCode() : 654311423)
                  );
            }

            if (!livingEntity.isSpectator()) {
               for (FeatureRenderer<T, M> feature : this.features) {
                  feature.render(matrixStack, vertexConsumerProvider, i, livingEntity, limbPos, limbSpeed, tickDelta, animationProgress, bodyOffset, pitch);
               }
            }

            matrixStack.pop();
            if (livingEntity instanceof Leashable leashable) {
               Entity entity2 = leashable.getLeashHolder();
               if (entity2 != null) {
                  this.__renderLeash(livingEntity, tickDelta, matrixStack, vertexConsumerProvider, entity2);
               }
            }

            if (this.hasLabel(livingEntity)) {
               this.renderLabelIfPresent(livingEntity, livingEntity.getDisplayName(), matrixStack, vertexConsumerProvider, i, tickDelta);
            }
         }
      }
   }

   @Inject(method = "setupTransforms", at = @At("HEAD"), cancellable = true)
   protected void setupTransformsHook(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale, CallbackInfo ci) {
      ci.cancel();
      if (this.isShaking(entity)) {
         bodyYaw += (float)(Math.cos(entity.age * 3.25) * Math.PI * 0.4F);
      }

      if (!entity.isInPose(EntityPose.SLEEPING)) {
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - bodyYaw));
      }

      if (entity.deathTime > 0 && !AnimationsModule.check5()) {
         float f = (entity.deathTime + tickDelta - 1.0F) / 20.0F * 1.6F;
         f = MathHelper.sqrt(f);
         if (f > 1.0F) {
            f = 1.0F;
         }

         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * this.getLyingAngle(entity)));
      } else if (entity.isUsingRiptide()) {
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - entity.getPitch()));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.age + tickDelta) * -75.0F));
      } else if (entity.isInPose(EntityPose.SLEEPING)) {
         Direction direction = entity.getSleepingDirection();
         float g = direction != null ? getYaw(direction) : bodyYaw;
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.getLyingAngle(entity)));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0F));
      } else if (shouldFlipUpsideDown(entity)) {
         matrices.translate(0.0F, (entity.getHeight() + 0.1F) / scale, 0.0F);
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      }
   }

   @Shadow
   public static boolean shouldFlipUpsideDown(LivingEntity entity) {
      return false;
   }

   @Shadow
   protected abstract void setupTransforms(T var1, MatrixStack var2, float var3, float var4, float var5, float var6);

   @ModifyExpressionValue(
      method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;")
   )
   private Entity hasLabel(Entity original) {
      return GameTiersModule.check2() ? null : original;
   }

   @Shadow
   protected abstract boolean hasLabel(T var1);

   @Shadow
   protected abstract float getLyingAngle(T var1);

   @Shadow
   @Nullable
   protected abstract RenderLayer getRenderLayer(T var1, boolean var2, boolean var3, boolean var4);
}
