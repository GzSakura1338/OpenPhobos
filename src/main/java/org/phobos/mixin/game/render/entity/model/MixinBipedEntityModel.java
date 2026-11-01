package org.phobos.mixin.game.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.earthhack.ducks.ILivingEntity;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.player.SneakAnimationState;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.render.AnimationsModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends LivingEntity> extends MixinEntityModel<T> implements EventSubscriber, MinecraftHolder {
   @Shadow
   @Final
   public ModelPart leftLeg;
   @Shadow
   public ArmPose leftArmPose;
   @Shadow
   @Final
   public ModelPart hat;
   @Shadow
   public float leaningPitch;
   @Shadow
   @Final
   public ModelPart rightArm;
   @Shadow
   @Final
   public ModelPart body;
   @Shadow
   @Final
   public ModelPart head;
   @Shadow
   @Final
   public ModelPart leftArm;
   @Shadow
   public ArmPose rightArmPose;
   @Shadow
   @Final
   public ModelPart rightLeg;
   @Shadow
   public boolean sneaking;

   @Shadow
   protected abstract void positionLeftArm(T var1);

   @Shadow
   protected abstract float lerpAngle(float var1, float var2, float var3);

   @Shadow
   protected abstract float method_2807(float var1);

   @Shadow
   protected abstract Arm getPreferredArm(T var1);

   @Shadow
   protected abstract void positionRightArm(T var1);

   @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
   public void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
      ci.cancel();
      boolean bl = this.getFallFlyingTicks(livingEntity) > 4;
      boolean bl2 = livingEntity.isInSwimmingPose();
      this.head.yaw = i * (float) (Math.PI / 180.0);
      if (bl) {
         this.head.pitch = (float) (-Math.PI / 4);
      } else if (this.leaningPitch > 0.0F) {
         if (bl2) {
            this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, (float) (-Math.PI / 4));
         } else {
            this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, j * (float) (Math.PI / 180.0));
         }
      } else {
         this.head.pitch = j * (float) (Math.PI / 180.0);
      }

      this.body.yaw = 0.0F;
      this.rightArm.pivotZ = 0.0F;
      this.rightArm.pivotX = -5.0F;
      this.leftArm.pivotZ = 0.0F;
      this.leftArm.pivotX = 5.0F;
      float k = 1.0F;
      if (bl) {
         k = (float)livingEntity.getVelocity().lengthSquared();
         k /= 0.2F;
         k *= k * k;
      }

      if (k < 1.0F) {
         k = 1.0F;
      }

      this.rightArm.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2.0F * g * 0.5F / k;
      this.leftArm.pitch = MathHelper.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
      this.rightArm.roll = 0.0F;
      this.leftArm.roll = 0.0F;
      this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
      this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * g / k;
      this.rightLeg.yaw = 0.005F;
      this.leftLeg.yaw = -0.005F;
      this.rightLeg.roll = 0.005F;
      this.leftLeg.roll = -0.005F;
      if (this.riding) {
         ModelPart var10000 = this.rightArm;
         var10000.pitch += (float) (-Math.PI / 5);
         var10000 = this.leftArm;
         var10000.pitch += (float) (-Math.PI / 5);
         this.rightLeg.pitch = -1.4137167F;
         this.rightLeg.yaw = (float) (Math.PI / 10);
         this.rightLeg.roll = 0.07853982F;
         this.leftLeg.pitch = -1.4137167F;
         this.leftLeg.yaw = (float) (-Math.PI / 10);
         this.leftLeg.roll = -0.07853982F;
      }

      this.rightArm.yaw = 0.0F;
      this.leftArm.yaw = 0.0F;
      boolean bl3 = livingEntity.getMainArm() == Arm.RIGHT;
      if (livingEntity.isUsingItem()) {
         boolean bl4 = livingEntity.getActiveHand() == Hand.MAIN_HAND;
         if (bl4 == bl3) {
            this.positionRightArm(livingEntity);
         } else {
            this.positionLeftArm(livingEntity);
         }
      } else {
         boolean bl4 = bl3 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
         if (bl3 != bl4) {
            this.positionLeftArm(livingEntity);
            this.positionRightArm(livingEntity);
         } else {
            this.positionRightArm(livingEntity);
            this.positionLeftArm(livingEntity);
         }
      }

      this.animateArms(livingEntity, h);
      SneakAnimationState event = this.invoke(new SneakAnimationState());
      if (event.getSneakAnimation() == null) {
         if (this.sneaking) {
            this.body.pitch = 0.5F;
            ModelPart var10000 = this.rightArm;
            var10000.pitch += 0.4F;
            var10000 = this.leftArm;
            var10000.pitch += 0.4F;
            this.rightLeg.pivotZ = 4.0F;
            this.leftLeg.pivotZ = 4.0F;
            this.rightLeg.pivotY = 12.2F;
            this.leftLeg.pivotY = 12.2F;
            this.head.pivotY = 4.2F;
            this.body.pivotY = 3.2F;
            this.leftArm.pivotY = 5.2F;
            this.rightArm.pivotY = 5.2F;
         } else {
            this.body.pitch = 0.0F;
            this.rightLeg.pivotZ = 0.0F;
            this.leftLeg.pivotZ = 0.0F;
            this.rightLeg.pivotY = 12.0F;
            this.leftLeg.pivotY = 12.0F;
            this.head.pivotY = 0.0F;
            this.body.pivotY = 0.0F;
            this.leftArm.pivotY = 2.0F;
            this.rightArm.pivotY = 2.0F;
         }
      } else {
         ILivingEntity accessor = (ILivingEntity)(Object)livingEntity;
         accessor.setPrevBodyPitch(this.body.pitch);
         accessor.setPrevRightLegPivotZ(this.rightLeg.pivotZ);
         accessor.setPrevLeftLegPivotZ(this.leftLeg.pivotZ);
         accessor.setPrevRightLegPivotY(this.rightLeg.pivotY);
         accessor.setPrevLeftLegPivotY(this.leftLeg.pivotY);
         accessor.setPrevHeadPivotY(this.head.pivotY);
         accessor.setPrevBodyPivotY(this.body.pivotY);
         accessor.setPrevLeftArmPivotY(this.leftArm.pivotY);
         accessor.setPrevRightArmPivotY(this.rightArm.pivotY);
         float targetBodyPitch = this.sneaking ? 0.5F : 0.0F;
         float targetRightLegPivotZ = this.sneaking ? 4.0F : 0.0F;
         float targetLeftLegPivotZ = this.sneaking ? 4.0F : 0.0F;
         float targetRightLegPivotY = this.sneaking ? 12.2F : 12.0F;
         float targetLeftLegPivotY = this.sneaking ? 12.2F : 12.0F;
         float targetHeadPivotY = this.sneaking ? 4.2F : 0.0F;
         float targetBodyPivotY = this.sneaking ? 3.2F : 0.0F;
         float targetLeftArmPivotY = this.sneaking ? 5.2F : 2.0F;
         float targetRightArmPivotY = this.sneaking ? 5.2F : 2.0F;
         float targetRightArmPitch = this.sneaking ? 0.4F : 0.0F;
         float targetLeftArmPitch = this.sneaking ? 0.4F : 0.0F;
         if (event.getSneakAnimation().equals(AnimationsModule.SneakAnimation.Custom)) {
            if (accessor.getBodyPitchAnimation() != null) {
               accessor.getBodyPitchAnimation().setFloatValue(targetBodyPitch);
            }

            if (accessor.getRightLegPivotZAnimation() != null) {
               accessor.getRightLegPivotZAnimation().setFloatValue(targetRightLegPivotZ);
            }

            if (accessor.getLeftLegPivotZAnimation() != null) {
               accessor.getLeftLegPivotZAnimation().setFloatValue(targetLeftLegPivotZ);
            }

            if (accessor.getRightLegPivotYAnimation() != null) {
               accessor.getRightLegPivotYAnimation().setFloatValue(targetRightLegPivotY);
            }

            if (accessor.getLeftLegPivotYAnimation() != null) {
               accessor.getLeftLegPivotYAnimation().setFloatValue(targetLeftLegPivotY);
            }

            if (accessor.getHeadPivotYAnimation() != null) {
               accessor.getHeadPivotYAnimation().setFloatValue(targetHeadPivotY);
            }

            if (accessor.getBodyPivotYAnimation() != null) {
               accessor.getBodyPivotYAnimation().setFloatValue(targetBodyPivotY);
            }

            if (accessor.getLeftArmPivotYAnimation() != null) {
               accessor.getLeftArmPivotYAnimation().setFloatValue(targetLeftArmPivotY);
            }

            if (accessor.getRightArmPivotYAnimation() != null) {
               accessor.getRightArmPivotYAnimation().setFloatValue(targetRightArmPivotY);
            }

            if (accessor.getRightArmPitchAnimation() != null) {
               accessor.getRightArmPitchAnimation().setFloatValue(targetRightArmPitch);
            }

            if (accessor.getLeftArmPitchAnimation() != null) {
               accessor.getLeftArmPitchAnimation().setFloatValue(targetLeftArmPitch);
            }

            if (accessor.getGlobalAnimation() != null) {
               accessor.getGlobalAnimation().setFloatValue(this.sneaking ? 1.0F : 0.0F);
               accessor.getGlobalAnimation().handleFloat3(10.0F, 0.04F);
            }

            if (accessor.getSneakAnimations() != null) {
               accessor.getSneakAnimations().handleFloat(50.0F, 0.08F);
            }
         }

         switch (event.getSneakAnimation()) {
            case Lerp:
               if (this.sneaking) {
                  ModelPart var10000x = this.rightArm;
                  var10000x.pitch += 0.4F;
                  var10000x = this.leftArm;
                  var10000x.pitch += 0.4F;
               }

               float tickDelta = mc.getRenderTickCounter().getTickDelta(false);
               this.body.pitch = MathHelper.lerp(tickDelta, accessor.getPrevBodyPitch(), targetBodyPitch);
               this.rightLeg.pivotZ = MathHelper.lerp(tickDelta, accessor.getPrevRightLegPivotZ(), targetRightLegPivotZ);
               this.leftLeg.pivotZ = MathHelper.lerp(tickDelta, accessor.getPrevLeftLegPivotZ(), targetLeftLegPivotZ);
               this.rightLeg.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevRightLegPivotY(), targetRightLegPivotY);
               this.leftLeg.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevLeftLegPivotY(), targetLeftLegPivotY);
               this.head.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevHeadPivotY(), targetHeadPivotY);
               this.body.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevBodyPivotY(), targetBodyPivotY);
               this.leftArm.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevLeftArmPivotY(), targetLeftArmPivotY);
               this.rightArm.pivotY = MathHelper.lerp(tickDelta, accessor.getPrevRightArmPivotY(), targetRightArmPivotY);
               break;
            case Custom:
               this.body.pitch = accessor.getBodyPitchAnimation().getFloatValue2();
               ModelPart var10000 = this.rightArm;
               var10000.pitch = var10000.pitch + accessor.getRightArmPitchAnimation().getFloatValue2();
               var10000 = this.leftArm;
               var10000.pitch = var10000.pitch + accessor.getLeftArmPitchAnimation().getFloatValue2();
               this.rightLeg.pivotZ = accessor.getRightLegPivotZAnimation().getFloatValue2();
               this.leftLeg.pivotZ = accessor.getLeftLegPivotZAnimation().getFloatValue2();
               this.rightLeg.pivotY = accessor.getRightLegPivotYAnimation().getFloatValue2();
               this.leftLeg.pivotY = accessor.getLeftLegPivotYAnimation().getFloatValue2();
               this.head.pivotY = accessor.getHeadPivotYAnimation().getFloatValue2();
               this.body.pivotY = accessor.getBodyPivotYAnimation().getFloatValue2();
               this.leftArm.pivotY = accessor.getLeftArmPivotYAnimation().getFloatValue2();
               this.rightArm.pivotY = accessor.getRightArmPivotYAnimation().getFloatValue2();
         }
      }

      if (this.rightArmPose != ArmPose.SPYGLASS) {
         CrossbowPosing.swingArm(this.rightArm, h, 1.0F);
      }

      if (this.leftArmPose != ArmPose.SPYGLASS) {
         CrossbowPosing.swingArm(this.leftArm, h, -1.0F);
      }

      if (this.leaningPitch > 0.0F) {
         float l = f % 26.0F;
         Arm arm = this.getPreferredArm(livingEntity);
         float m = arm == Arm.RIGHT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
         float n = arm == Arm.LEFT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
         if (!livingEntity.isUsingItem()) {
            if (l < 14.0F) {
               this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 0.0F);
               this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 0.0F);
               this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, (float) Math.PI);
               this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, (float) Math.PI);
               this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, (float) Math.PI + 1.8707964F * this.method_2807(l) / this.method_2807(14.0F));
               this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, (float) Math.PI - 1.8707964F * this.method_2807(l) / this.method_2807(14.0F));
            } else if (l >= 14.0F && l < 22.0F) {
               float o = (l - 14.0F) / 8.0F;
               this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, (float) (Math.PI / 2) * o);
               this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, (float) (Math.PI / 2) * o);
               this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, (float) Math.PI);
               this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, (float) Math.PI);
               this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 5.012389F - 1.8707964F * o);
               this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 1.2707963F + 1.8707964F * o);
            } else if (l >= 22.0F && l < 26.0F) {
               float o = (l - 22.0F) / 4.0F;
               this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, (float) (Math.PI / 2) - (float) (Math.PI / 2) * o);
               this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, (float) (Math.PI / 2) - (float) (Math.PI / 2) * o);
               this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, (float) Math.PI);
               this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, (float) Math.PI);
               this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, (float) Math.PI);
               this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, (float) Math.PI);
            }
         }

         float o = 0.3F;
         float p = 0.33333334F;
         this.leftLeg.pitch = MathHelper.lerp(this.leaningPitch, this.leftLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F + (float) Math.PI));
         this.rightLeg.pitch = MathHelper.lerp(this.leaningPitch, this.rightLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F));
      }

      this.hat.copyTransform(this.head);
   }

   @Unique
   private int getFallFlyingTicks(T livingEntity) {
      return livingEntity instanceof ClientPlayerEntity && PlayerActionUtils.check() ? 0 : livingEntity.getFallFlyingTicks();
   }

   @Shadow
   protected abstract void animateArms(T var1, float var2);
}
