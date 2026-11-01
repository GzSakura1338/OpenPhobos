package org.phobos.mixin.game.render.player;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PlayerAnimationEvent;
import org.phobos.module.player.LegacyModule;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.InitEvent2;
import org.phobos.event.RenderArmEvent;
import org.phobos.mixin.game.render.interfaces.ILimbAnimator;
import org.phobos.module.render.AnimationsModule;
import org.phobos.module.render.ChamsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer
   extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
   implements EventSubscriber,
   MinecraftHolder {
   @Overwrite
   @SuppressWarnings("overwrite")
   public void render(
      AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i
   ) {
      this.setModelPose(abstractClientPlayerEntity);
      if (AnimationsModule.check2()) {
         abstractClientPlayerEntity.limbAnimator.setSpeed(0.0F);
         ((ILimbAnimator)(Object)abstractClientPlayerEntity.limbAnimator).setPos(0.0F);
      }

      if (abstractClientPlayerEntity.isMainPlayer() && !ChamsModule.flag) {
         PlayerAnimationEvent event = this.invoke(new PlayerAnimationEvent(abstractClientPlayerEntity));
         if (event.isCancelled()) {
            float yaw = abstractClientPlayerEntity.getYaw();
            float prevYaw = abstractClientPlayerEntity.prevYaw;
            float bodyYaw = abstractClientPlayerEntity.bodyYaw;
            float prevBodyYaw = abstractClientPlayerEntity.prevBodyYaw;
            float headYaw = abstractClientPlayerEntity.headYaw;
            float prevHeadYaw = abstractClientPlayerEntity.prevHeadYaw;
            float pitch = abstractClientPlayerEntity.getPitch();
            float prevPitch = abstractClientPlayerEntity.prevPitch;
            abstractClientPlayerEntity.setYaw(event.getFloatValue());
            abstractClientPlayerEntity.prevYaw = event.getFloatValue32();
            abstractClientPlayerEntity.setBodyYaw(event.getFloatValue3());
            abstractClientPlayerEntity.prevBodyYaw = event.getFloatValue4();
            abstractClientPlayerEntity.setHeadYaw(event.getFloatValue());
            abstractClientPlayerEntity.prevHeadYaw = event.getFloatValue32();
            abstractClientPlayerEntity.setPitch(event.getFloatValue2());
            abstractClientPlayerEntity.prevPitch = event.getFloatValue6();
            super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
            abstractClientPlayerEntity.setYaw(yaw);
            abstractClientPlayerEntity.prevYaw = prevYaw;
            abstractClientPlayerEntity.setBodyYaw(bodyYaw);
            abstractClientPlayerEntity.prevBodyYaw = prevBodyYaw;
            abstractClientPlayerEntity.setHeadYaw(headYaw);
            abstractClientPlayerEntity.prevHeadYaw = prevHeadYaw;
            abstractClientPlayerEntity.setPitch(pitch);
            abstractClientPlayerEntity.prevPitch = prevPitch;
            return;
         }
      }

      super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
   }

   @Redirect(
      method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isFallFlying()Z")
   )
   private boolean isFallFlyingHook(AbstractClientPlayerEntity instance) {
      return EntityUtils.testLivingEntity3(instance);
   }

   public MixinPlayerEntityRenderer(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
      super(ctx, model, shadowRadius);
      throw new IllegalStateException("MixinPlayerEntityRenderer constructor called!");
   }

   @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true)
   private void renderArmHook(
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      AbstractClientPlayerEntity player,
      ModelPart arm,
      ModelPart sleeve,
      CallbackInfo ci
   ) {
      this.invoke(new RenderArmEvent(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>)(Object)this.getModel(), player, arm, sleeve), ci);
   }

   @Shadow
   protected abstract void setModelPose(AbstractClientPlayerEntity var1);

   @Inject(method = "<init>", at = @At("RETURN"))
   private void initHook(Context ctx, boolean slim, CallbackInfo ci) {
      this.addFeature(new InitEvent2(this));
   }

   @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
   private static void getArmPoseHook(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<ArmPose> cir) {
      if (player.isMainPlayer()) {
         if (LegacyModule.check2()) {
            cir.setReturnValue(Hand.MAIN_HAND.equals(hand) ? ArmPose.BLOCK : ArmPose.EMPTY);
         }
      }
   }
}
