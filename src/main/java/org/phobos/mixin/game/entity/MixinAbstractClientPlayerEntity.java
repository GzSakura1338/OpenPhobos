package org.phobos.mixin.game.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.earthhack.ducks.IAbstractClientPlayerEntity;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.PlayerTextureCache;
import org.phobos.core.player.PlayerAnimationState;
import org.phobos.core.player.PlayerTextureCacheImpl;
import org.phobos.core.prediction.SampleBuffer;
import org.phobos.core.prediction.PredictionManager;
import org.phobos.core.fakeplayer.FakePlayer;
import org.phobos.core.fakeplayer.TrackedFakePlayer;
import org.phobos.module.client.EngineModule;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity
   extends MixinLivingEntity
   implements EventSubscriber,
   IDamageProtectionEntity,
   IAbstractClientPlayerEntity {
   @Unique
   private int jumpScore;
   @Unique
   private final PlayerTextureCache movementModel = new PlayerTextureCacheImpl();
   @Unique
   private final SampleBuffer history = new SampleBuffer(20);
   @Unique
   private final TrackedFakePlayer[] predictions = new TrackedFakePlayer[7];
   @Unique
   private final PlayerAnimationState entityData = new PlayerAnimationState();
   @Unique
   private int intValue;

   @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
   private void getSkinTexturesHook(CallbackInfoReturnable<SkinTextures> cir) {
      if ((Object)this instanceof FakePlayer fakePlayer && fakePlayer.getSkinTextures2() != null) {
         cir.setReturnValue(fakePlayer.getSkinTextures2());
      }
   }

   @Override
   public PlayerAnimationState getEntityData() {
      return this.entityData;
   }

   @Override
   public int getJumpScore() {
      return this.jumpScore;
   }

   @Inject(method = "getFovMultiplier", at = @At("HEAD"), cancellable = true)
   private void fovHook(CallbackInfoReturnable<Float> cir) {
      NoRenderModule.Fov event = this.invoke(new NoRenderModule.Fov());
      if (event.isCancelled()) {
         cir.setReturnValue(1.0F);
      }
   }

   @Override
   public void setJumpScore(int score) {
      this.jumpScore = score;
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void tickHook(CallbackInfo ci) {
      if (mc.player != null && EngineModule.getEngineModule().check2()) {
         Entity entity = (Entity)(Object)this;
         if (entity instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) {
            this.history.clear();
         } else if (entity.squaredDistanceTo(mc.player) > PredictionManager.doubleValue) {
            this.history.clear();
         } else {
            this.history.handleEntity(entity);
         }
      }
   }

   @Override
   public void setIntValue(int damageProtection) {
      this.intValue = damageProtection;
   }

   @Override
   public int getIntValue() {
      return this.intValue;
   }

   @Override
   public void addJumpScore() {
      this.jumpScore++;
   }

   @Override
   public SampleBuffer getHistory() {
      return this.history;
   }

   @Override
   public TrackedFakePlayer[] getPredictions() {
      return this.predictions;
   }

   @Override
   public PlayerTextureCache getMovementModel() {
      return this.movementModel;
   }

}
