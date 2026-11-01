package org.phobos.mixin.game.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.Animatable;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.PlayerAnimator;
import org.phobos.core.event.events.TravelEvent;
import org.phobos.core.event.events.TravelEvent;
import org.phobos.core.event.events.TravelEvent;
import org.phobos.core.math.Vector3;
import org.phobos.core.player.PlayerStats;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.core.user.GameTiersApi;
import org.phobos.event.AttackEvent;
import org.phobos.event.DropItemEvent;
import org.phobos.event.JumpEvent3;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.client.GameTiersModule;
import org.phobos.module.combat.KeepSprintModule;
import org.phobos.module.player.ReachModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity implements MinecraftHolder, EventSubscriber, PlayerAnimator {
   @Unique
   private Animatable simulation;
   @Unique
   private boolean dirty;
   @Unique
   private Vector3 lastPlayerAnimatorPosition;

   @Override
   public Vector3 getLastPlayerAnimatorPosition() {
      return this.lastPlayerAnimatorPosition;
   }

   @Override
   public Animatable getSimulation() {
      return this.simulation;
   }

   @Unique
   private void travel(TravelEvent event, CallbackInfo ci) {
      if (event.isCancelled() && mc.player != null) {
         mc.player.move(MovementType.SELF, mc.player.getVelocity());
         ci.cancel();
      }
   }

   @Inject(method = "travel", at = @At("RETURN"), cancellable = true)
   private void travelHook2(Vec3d vec3d, CallbackInfo ci) {
      if (this.isMainPlayer()) {
         this.travel(this.invoke(new TravelEvent.Post(vec3d)), ci);
      }
   }

   @Inject(method = "getBaseDimensions", at = @At("HEAD"), cancellable = true)
   private void getBaseDimensionsHook(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
      if (EntityPose.CROUCHING.equals(pose)) {
         AntiCheatModule.Client.handleConsumer(cir::setReturnValue);
      }
   }

   @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
   private void travelHook1(Vec3d vec3d, CallbackInfo ci) {
      if (this.isMainPlayer()) {
         this.travel(this.invoke(new TravelEvent.Pre(vec3d)), ci);
      }
   }

   @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
   private void jumpHook(CallbackInfo ci) {
      if (this.isMainPlayer()) {
         this.invoke(new JumpEvent3(), ci);
      }
   }

   @Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
   public void interactionRangeHook(CallbackInfoReturnable<Double> cir) {
      Object o = this;
      PlayerEntity entity = (PlayerEntity)o;
      if (entity.isMainPlayer()) {
         cir.setReturnValue(ReachModule.computeDouble((Double)cir.getReturnValue()));
      }
   }

   @Inject(method = "updateCapeAngles", at = @At("HEAD"))
   private void moveCloakUpdate(CallbackInfo info) {
      PlayerEntity playerDelegate = (PlayerEntity)(Object)this;
      if (playerDelegate instanceof AbstractClientPlayerEntity player) {
         this.handleInt(16);
         PlayerStats playerDelegatex = new PlayerStats(player);
         if (this.dirty) {
            this.dirty = false;
            this.simulation.handleVector3(new Vector3(1.0F, 1.0F, 0.0F));

            for (int i = 0; i < 5; i++) {
               this.handleNumericStats(playerDelegatex);
            }
         }

         this.handleNumericStats(playerDelegatex);
      }
   }

   @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F"))
   private float attackHook(float original) {
      AttackEvent event = this.invoke(new AttackEvent(original));
      return event.getFloatValue();
   }

   @Redirect(
      method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V")
   )
   private void dropItemHook(PlayerEntity player, Hand hand) {
      DropItemEvent event = this.invoke(new DropItemEvent());
      if (!event.isCancelled()) {
         player.swingHand(hand);
      }
   }

   @Override
   public void setSimulation(Animatable simulation) {
      this.simulation = simulation;
   }

   @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
   public Text prependTier(Text original) {
      if (GameTiersModule.getGameTiersModule().check7()) {
         PlayerEntity self = (PlayerEntity)(Object)this;
         return GameTiersApi.describePlayerEntity(self, original);
      } else {
         return original;
      }
   }

   @Shadow
   public abstract float getAttackCooldownProgress(float var1);

   @ModifyExpressionValue(
      method = "adjustMovementForSneaking",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;clipAtLedge()Z")
   )
   private boolean clipAtLedgeHook(boolean original) {
      return !this.isMainPlayer() ? original : SwitchOverride.testFlag(original);
   }

   @Inject(method = "getEntityInteractionRange", at = @At("RETURN"), cancellable = true)
   public void entityInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      Object o = this;
      PlayerEntity entity = (PlayerEntity)o;
      if (entity.isMainPlayer()) {
         cir.setReturnValue(ReachModule.computeDouble((Double)cir.getReturnValue()));
      }
   }

   @Override
   public void setLastPlayerAnimatorPosition(Vector3 lastPlayerAnimatorPosition) {
      this.lastPlayerAnimatorPosition = lastPlayerAnimatorPosition;
   }

   @Override
   public void run() {
      this.dirty = true;
   }

   @Redirect(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0)
   )
   private void attackHook1(PlayerEntity player, Vec3d velocity) {
      if (player.isMainPlayer()) {
         KeepSprintModule.SprintResetEvent event = this.invoke(new KeepSprintModule.SprintResetEvent());
         if (event.isCancelled()) {
            return;
         }
      }

      player.setVelocity(velocity);
   }

   @Shadow
   public abstract boolean isMainPlayer();

   @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", ordinal = 0))
   private void attackHook2(PlayerEntity player, boolean sprint) {
      if (player.isMainPlayer()) {
         KeepSprintModule.SprintResetEvent event = this.invoke(new KeepSprintModule.SprintResetEvent());
         if (event.isCancelled()) {
            return;
         }
      }

      player.setSprinting(sprint);
   }
}
