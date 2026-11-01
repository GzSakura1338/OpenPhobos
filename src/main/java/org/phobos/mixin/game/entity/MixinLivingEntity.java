package org.phobos.mixin.game.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.earthhack.ducks.IDamageProtectionEntity;
import org.earthhack.ducks.ITotemPoppingEntity;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.core.render.PlayerRenderState;
import org.phobos.core.animation.AnimatorGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.BaseTickEvent;
import org.phobos.event.ClearActiveItemEvent;
import org.phobos.event.EatFoodEvent;
import org.phobos.event.GetStepHeightEvent;
import org.phobos.event.JumpEvent;
import org.phobos.event.JumpEvent2;
import org.phobos.event.JumpEvent2;
import org.phobos.event.SwingSpeedEvent;
import org.phobos.event.TrackedPositionUpdateEvent;
import org.phobos.module.player.AntiClimbModule;
import org.phobos.module.player.AntiLevitationModule;
import org.phobos.module.client.CalculationsModule;
import org.phobos.module.movement.ElytraFlyModule;
import org.phobos.module.client.RotationsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity
   extends MixinEntity
   implements EventSubscriber,
   IDamageProtectionEntity,
   ITotemPoppingEntity,
   org.earthhack.ducks.ILivingEntity {
   @Unique
   private final FloatAnimator rightLegPivotZAnimation = new FloatAnimator();
   @Unique
   private final FloatAnimator bodyPitchAnimation = new FloatAnimator();
   @Unique
   private float prevHeadPivotY;
   @Unique
   private final FloatAnimator leftLegPivotZAnimation = new FloatAnimator();
   @Unique
   private PlayerRenderState ghostModel;
   @Unique
   private float prevRightArmPivotY;
   @Unique
   private float prevLeftArimPitch;
   @Unique
   private float prevRightArmPitch;
   @Unique
   private final FloatAnimator rightLegPivotYAnimation = new FloatAnimator(12.0F, 12.0F);
   @Unique
   private float prevBodyPitch;
   @Unique
   private float prevLeftArmPivotY;
   @Unique
   private float prevLeftLegPivotY;
   @Unique
   private float prevBodyPivotY;
   @Unique
   private final FloatAnimator leftLegPivotYAnimation = new FloatAnimator(12.0F, 12.0F);
   @Unique
   private final FloatAnimator headPivotYAnimation = new FloatAnimator();
   @Unique
   private float prevRightLegPivotZ;
   @Unique
   private float prevLeftLegPivotZ;
   @Unique
   private float prevRightLegPivotY;
   @Unique
   private final FloatAnimator bodyPivotYAnimation = new FloatAnimator();
   @Unique
   private final FloatAnimator leftArmPivotYAnimation = new FloatAnimator(2.0F, 2.0F);
   @Unique
   private boolean $sneaking;
   @Unique
   private float lastAttackCooldown;
   @Unique
   private int lastOffhandCount;
   @Unique
   private final FloatAnimator rightArmPivotYAnimation = new FloatAnimator(2.0F, 2.0F);
   @Unique
   private ItemStack itemStack;
   @Unique
   private final FloatAnimator rightArmPitchAnimation = new FloatAnimator(0.0F, 0.0F);
   @Unique
   private final FloatAnimator leftArmPitchAnimation = new FloatAnimator(0.0F, 0.0F);
   @Unique
   private int lastMainhandCount;
   @Unique
   private ItemStack itemStack2;
   @Unique
   private final FloatAnimator globalAnimation = new FloatAnimator(0.0F, 0.0F);
   @Shadow
   private int jumpingCooldown;
   @Unique
   private boolean oldElytra;
   @Shadow
   protected int lastAttackedTicks;
   @Unique
   private final AnimatorGroup sneakAnimations = new AnimatorGroup(
            this.bodyPitchAnimation,
            this.rightLegPivotZAnimation,
            this.leftLegPivotZAnimation,
            this.rightLegPivotYAnimation,
            this.leftLegPivotYAnimation,
            this.headPivotYAnimation,
            this.bodyPivotYAnimation,
            this.leftArmPivotYAnimation,
            this.rightArmPivotYAnimation,
            this.rightArmPitchAnimation,
            this.leftArmPitchAnimation
         );
   @Unique
   private int intValue2;
   @Shadow
   protected ItemStack activeItemStack;
   @Unique
   private final FloatAnimator floatAnimator = new FloatAnimator();
   @Unique
   private long lastLerp = 0L;
   @Unique
   private final TimeGate.Normal lastTotemPop = new TimeGate.Normal();

   @Shadow
   public abstract boolean damage(DamageSource var1, float var2);

   @Override
   public void setIntValue(int damageProtection) {
      this.intValue2 = damageProtection;
   }

   @Shadow
   public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> var1);

   @Shadow
   public abstract double getAttributeValue(RegistryEntry<EntityAttribute> var1);


   @Override
   public void setLastMainhandStack(ItemStack stack) {
      this.itemStack2 = stack;
   }

   @Shadow
   @Nullable
   public abstract StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> var1);

   @Shadow
   public abstract boolean hasNoDrag();

   @Override
   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @Override
   public void setLastLerp(long time) {
      this.lastLerp = time;
   }

   @Override
   public void setLastOffhandStack(ItemStack stack) {
      this.itemStack = stack;
   }

   @Override
   public int getLastMainhandCount() {
      return this.lastMainhandCount;
   }

   @Override
   public void setLastMainhandCount(int count) {
      this.lastMainhandCount = count;
   }

   @Override
   public ItemStack getItemStack2() {
      return this.itemStack2;
   }

   @Override
   public float getLastAttackCooldown() {
      return MathHelper.clamp((this.lastAttackCooldown + 0.5F) / this.__getAttackCooldownProgressPerTick(), 0.0F, 1.0F);
   }

   @Override
   public int getIntValue() {
      return switch (CalculationsModule.getDamage()) {
         case Normal, Enchants -> this.intValue2;
         case Generic -> 32;
      };
   }

   @Shadow
   @Nullable
   public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> var1);

   public TimeGate.Normal lastTotemPop() {
      return this.lastTotemPop;
   }

   @Shadow
   public abstract Vec3d applyMovementInput(Vec3d var1, float var2);

   @Shadow
   public abstract float getYaw(float var1);

   @Unique
   private float __getAttackCooldownProgressPerTick() {
      return (float)(1.0 / this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) * 20.0);
   }

   @Override
   public void setGhostModel(PlayerRenderState ghostModel) {
      this.ghostModel = ghostModel;
   }

   @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
   private float jumpHook1(LivingEntity instance) {
      JumpEvent2 event = this.invoke(new JumpEvent2(this.getYaw()));
      return event.getFloatValue();
   }

   @ModifyExpressionValue(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F"))
   private float jumpHook(float original) {
      return this.invoke(new JumpEvent(original)).getFloatValue();
   }

   @Redirect(
      method = "travel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
   )
   private boolean travelHook1(LivingEntity instance, RegistryEntry<StatusEffect> effect) {
      if (effect != null && (effect.equals(StatusEffects.LEVITATION) || effect.equals(StatusEffects.SLOW_FALLING)) && AntiLevitationModule.check()) {
         instance.fallDistance = 0.0F;
         return false;
      } else {
         return instance.hasStatusEffect(effect);
      }
   }

   @Inject(method = "getStepHeight", at = @At("RETURN"), cancellable = true)
   public void getStepHeightHook(CallbackInfoReturnable<Float> cir) {
      if ((Object)this == mc.player) {
         GetStepHeightEvent event = this.invoke(new GetStepHeightEvent((Float)cir.getReturnValue()));
         cir.setReturnValue(event.getFloatValue());
      }
   }

   @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
   private void swingSpeedHook(CallbackInfoReturnable<Integer> cir) {
      SwingSpeedEvent event = this.invoke(new SwingSpeedEvent(LivingEntity.class.cast(this), (Integer)cir.getReturnValue()));
      cir.setReturnValue(event.getIntValue());
   }

   @Inject(
      method = "travel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getVelocityAffectingPos()Lnet/minecraft/util/math/BlockPos;"),
      cancellable = true
   )
   private void travelHook4(Vec3d movementInput, CallbackInfo ci) {
      if (AntiLevitationModule.check2()) {
         ci.cancel();
         double d = this.getFinalGravity();
         boolean bl = this.getVelocity().y <= 0.0;
         if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            d = Math.min(d, 0.01);
         }

         BlockPos blockPos = this.getVelocityAffectingPos();
         float p = this.getWorld().getBlockState(blockPos).getBlock().getSlipperiness();
         float f = this.isOnGround() ? p * 0.91F : 0.91F;
         Vec3d vec3d6 = this.applyMovementInput(movementInput, p);
         double q = vec3d6.y;
         if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
            int input = AntiLevitationModule.value();
            switch (input) {
               case -1:
                  q = -(0.3 * (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
                  break;
               case 0:
                  q = 0.0;
                  break;
               case 1:
                  q = (0.3 * (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
            }
         } else if (!this.getWorld().isClient || this.getWorld().isChunkLoaded(blockPos)) {
            q -= d;
         } else if (this.getY() > this.getWorld().getBottomY()) {
            q = -0.1;
         } else {
            q = 0.0;
         }

         if (this.hasNoDrag()) {
            this.setVelocity(vec3d6.x, q, vec3d6.z);
         } else {
            this.setVelocity(vec3d6.x * f, (Object)this instanceof Flutterer ? q * f : q * 0.98F, vec3d6.z * f);
         }
      }
   }

   @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
   private boolean travelHook(LivingEntity instance) {
      return EntityUtils.testLivingEntity2(instance);
   }

   @Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
   private void isClimbingHook(CallbackInfoReturnable<Boolean> cir) {
      if (AntiClimbModule.check()) {
         cir.setReturnValue(false);
      }
   }

   @Override
   public void setLastOffhandCount(int count) {
      this.lastOffhandCount = count;
   }

   @Inject(method = "updateTrackedPositionAndAngles", at = @At("HEAD"))
   private void trackedPositionUpdateHook(double x, double y, double z, float yaw, float pitch, int interpolationSteps, CallbackInfo ci) {
      this.invoke(new TrackedPositionUpdateEvent(LivingEntity.class.cast(this), x, y, z, yaw, pitch, this.lastLerp));
      this.lastLerp = TimeUtils.value();
   }

   @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
   private float travelHook2(LivingEntity instance) {
      RotationManager.Rotation rotation = RotationManager.getRotation();
      return !rotation.check() && RotationsModule.check2() ? rotation.getFloatValue() : instance.getPitch();
   }

   @Override
   public PlayerRenderState getGhostModel() {
      return this.ghostModel;
   }

   @Inject(method = "clearActiveItem", at = @At("HEAD"))
   private void clearActiveItemHook(CallbackInfo ci) {
      if ((Object)this instanceof ClientPlayerEntity) {
         this.invoke(new ClearActiveItemEvent(this.activeItemStack));
      }
   }

   @Override
   public int getLastOffhandCount() {
      return this.lastOffhandCount;
   }

   @Inject(method = "isFallFlying", at = @At("TAIL"), cancellable = true)
   private void isFallFlyingHook(CallbackInfoReturnable<Boolean> cir) {
      if (ElytraFlyModule.check()) {
         boolean elytra = (Boolean)cir.getReturnValue();
         if (this.oldElytra && !elytra) {
            cir.setReturnValue(ElytraFlyModule.check2());
         }

         this.oldElytra = elytra;
      }
   }

   @Inject(method = "jump", at = @At("RETURN"))
   private void jumpHook2(CallbackInfo ci) {
      this.invoke(new JumpEvent2.Post());
   }

   @Inject(method = "baseTick", at = @At("HEAD"))
   public void baseTickHook(CallbackInfo ci) {
      BaseTickEvent event = this.invoke(new BaseTickEvent(this.jumpingCooldown));
      this.jumpingCooldown = event.getIntValue();
   }

   @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"))
   private void swingHandHook(Hand hand, CallbackInfo ci) {
      if (hand == Hand.MAIN_HAND) {
         this.lastAttackCooldown = this.lastAttackedTicks;
      }
   }

   @Inject(method = "eatFood", at = @At("HEAD"))
   private void eatFoodHook(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
      this.invoke(new EatFoodEvent((LivingEntity)(Object)this, foodComponent, stack));
   }

   @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
   private Vec3d travelHook3(LivingEntity instance) {
      RotationManager.Rotation rotation = RotationManager.getRotation();
      return !rotation.check() && RotationsModule.check2() ? rotation.getVec3d() : instance.getRotationVector();
   }

   @Override
   public void setPrevBodyPivotY(float prevBodyPivotY) {
      this.prevBodyPivotY = prevBodyPivotY;
   }

   @Override
   public float getPrevLeftArmPivotY() {
      return this.prevLeftArmPivotY;
   }

   @Override
   public float getPrevBodyPivotY() {
      return this.prevBodyPivotY;
   }

   @Override
   public float getPrevHeadPivotY() {
      return this.prevHeadPivotY;
   }

   @Override
   public float getPrevBodyPitch() {
      return this.prevBodyPitch;
   }

   public float getPrevLeftArimPitch() {
      return this.prevLeftArimPitch;
   }

   @Override
   public void setPrevHeadPivotY(float prevHeadPivotY) {
      this.prevHeadPivotY = prevHeadPivotY;
   }

   @Override
   public void setPrevRightLegPivotY(float prevRightLegPivotY) {
      this.prevRightLegPivotY = prevRightLegPivotY;
   }

   @Override
   public float getPrevRightLegPivotZ() {
      return this.prevRightLegPivotZ;
   }

   @Override
   public void setPrevLeftArmPivotY(float prevLeftArmPivotY) {
      this.prevLeftArmPivotY = prevLeftArmPivotY;
   }

   @Override
   public void setPrevBodyPitch(float prevBodyPitch) {
      this.prevBodyPitch = prevBodyPitch;
   }

   @Override
   public void setPrevLeftLegPivotY(float prevLeftLegPivotY) {
      this.prevLeftLegPivotY = prevLeftLegPivotY;
   }

   @Override
   public float getPrevLeftLegPivotZ() {
      return this.prevLeftLegPivotZ;
   }

   @Override
   public void setPrevRightArmPivotY(float prevRightArmPivotY) {
      this.prevRightArmPivotY = prevRightArmPivotY;
   }

   @Override
   public void setPrevLeftLegPivotZ(float prevLeftLegPivotZ) {
      this.prevLeftLegPivotZ = prevLeftLegPivotZ;
   }

   public void setPrevRightArmPitch(float prevRightArmPitch) {
      this.prevRightArmPitch = prevRightArmPitch;
   }

   @Override
   public float getPrevRightLegPivotY() {
      return this.prevRightLegPivotY;
   }

   @Override
   public float getPrevLeftLegPivotY() {
      return this.prevLeftLegPivotY;
   }

   @Override
   public void setPrevRightLegPivotZ(float prevRightLegPivotZ) {
      this.prevRightLegPivotZ = prevRightLegPivotZ;
   }

   @Override
   public float getPrevRightArmPitch() {
      return this.prevRightArmPitch;
   }

   @Override
   public float getPrevRightArmPivotY() {
      return this.prevRightArmPivotY;
   }

   @Override
   public FloatAnimator getBodyPivotYAnimation() {
      return this.bodyPivotYAnimation;
   }

   @Override
   public FloatAnimator getBodyPitchAnimation() {
      return this.bodyPitchAnimation;
   }

   @Override
   public FloatAnimator getRightArmPitchAnimation() {
      return this.rightArmPitchAnimation;
   }

   @Override
   public FloatAnimator getLeftLegPivotYAnimation() {
      return this.leftLegPivotYAnimation;
   }

   @Override
   public FloatAnimator getRightLegPivotYAnimation() {
      return this.rightLegPivotYAnimation;
   }

   @Override
   public void set$sneaking(boolean $sneaking) {
      this.$sneaking = $sneaking;
   }

   @Override
   public FloatAnimator getLeftArmPivotYAnimation() {
      return this.leftArmPivotYAnimation;
   }

   @Override
   public FloatAnimator getHeadPivotYAnimation() {
      return this.headPivotYAnimation;
   }

   @Override
   public FloatAnimator getRightLegPivotZAnimation() {
      return this.rightLegPivotZAnimation;
   }

   public void setPrevLeftArimPitch(float prevLeftArimPitch) {
      this.prevLeftArimPitch = prevLeftArimPitch;
   }

   @Override
   public FloatAnimator getLeftLegPivotZAnimation() {
      return this.leftLegPivotZAnimation;
   }

   @Override
   public FloatAnimator getLeftArmPitchAnimation() {
      return this.leftArmPitchAnimation;
   }

   @Override
   public AnimatorGroup getSneakAnimations() {
      return this.sneakAnimations;
   }

   @Override
   public FloatAnimator getGlobalAnimation() {
      return this.globalAnimation;
   }

   @Override
   public boolean is$sneaking() {
      return this.$sneaking;
   }

   @Override
   public FloatAnimator getRightArmPivotYAnimation() {
      return this.rightArmPivotYAnimation;
   }

   @Override
   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }
}
