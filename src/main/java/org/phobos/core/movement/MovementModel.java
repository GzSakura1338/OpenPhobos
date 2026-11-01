package org.phobos.core.movement;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;
import org.phobos.core.entity.EntityUtils;

public class MovementModel {
   public static float floatValue = 2.0F;
   public static final double doubleValue8 = 0.287;
   public static final double doubleValue9 = 10.0;
   public static final double doubleValue10 = 0.98;
   public static final double doubleValue11 = 0.08;

   public static double value() {
      return 0.287;
   }

   public double value2() {
      return 10.0;
   }

   public MovementModel.State resolveState(PlayerEntity playerEntity, CollisionView collisionView, MovementModel.State state, Vec3d vec3d) {
      return this.resolveState2(playerEntity, collisionView, state, vec3d);
   }

   public void handleFloat(float f) {
      floatValue = f;
   }

   public double getDoubleValue() {
      return 0.287;
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity) {
      return playerEntity.fallDistance > 5.0
         || playerEntity.isTouchingWater()
         || playerEntity.inPowderSnow
         || playerEntity.isInLava()
         || playerEntity.isClimbing()
         || EntityUtils.testLivingEntity2(playerEntity);
   }

   public double value3() {
      return 0.08;
   }

   public boolean testLivingEntity(LivingEntity livingEntity) {
      return livingEntity.isOnGround()
         && livingEntity.verticalCollision
         && livingEntity.fallDistance < 0.1
         && !livingEntity.isSubmergedInWater()
         && !livingEntity.isClimbing()
         && !livingEntity.isInLava();
   }

   public double computePlayerEntity(PlayerEntity playerEntity) {
      double d = this.getDoubleValue();
      StatusEffectInstance statusEffectInstance = playerEntity.getStatusEffect(StatusEffects.SPEED);
      if (statusEffectInstance != null) {
         d *= 1.0 + 0.2 * (statusEffectInstance.getAmplifier() + 1);
      }

      StatusEffectInstance statusEffectInstance2 = playerEntity.getStatusEffect(StatusEffects.SLOWNESS);
      if (statusEffectInstance2 != null) {
         d /= 1.0 + 0.2 * (statusEffectInstance2.getAmplifier() + 1);
      }

      return d;
   }

   public double value4() {
      return 0.98;
   }

   public MovementModel.State resolveState2(PlayerEntity playerEntity, CollisionView collisionView, MovementModel.State state, Vec3d vec3d) {
      MovementModel.State state2 = state.createState();
      state2.setFlag2(false);
      if (testPlayerEntity(playerEntity)) {
         state2.run();
         return state2;
      } else {
         state2.doubleValue = this.computePlayerEntity(playerEntity);
         state2.vec3d = new Vec3d(state2.doubleValue * vec3d.x, vec3d.y, state2.doubleValue * vec3d.z);
         state2.intValue = 0;
         return state2;
      }
   }

   public double value5() {
      return 0.0784;
   }

   public float value6() {
      return floatValue;
   }

   public static class State {
      public double doubleValue;
      public boolean flag;
      public double doubleValue2;
      public int intValue;
      public boolean flag2;
      public Vec3d vec3d;

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setDoubleValue2(double d) {
         this.doubleValue2 = d;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public State(double d, double d2, Vec3d vec3d, int i, boolean flag, boolean flag2) {
         this.doubleValue = d;
         this.doubleValue2 = d2;
         this.vec3d = vec3d;
         this.intValue = i;
         this.flag = flag;
         this.flag2 = flag2;
      }

      public State() {
         this(0.0, 0.0, Vec3d.ZERO, 0, false, false);
      }

      public void run() {
         this.doubleValue = 0.0;
         this.intValue = 0;
         this.doubleValue2 = 0.0;
         this.vec3d = Vec3d.ZERO;
         this.flag = false;
         this.flag2 = true;
      }

      public double getDoubleValue2() {
         return this.doubleValue2;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      @Override
      public boolean equals(Object object) {
         if (object == this) {
            return true;
         } else if (!(object instanceof MovementModel.State state)) {
            return false;
         } else {
            if (!state.testObject(this)) {
               return false;
            }

            if (Double.compare(this.getDoubleValue(), state.getDoubleValue()) != 0) {
               return false;
            }

            if (Double.compare(this.getDoubleValue2(), state.getDoubleValue2()) != 0) {
               return false;
            }

            if (this.getIntValue() != state.getIntValue()) {
               return false;
            }

            if (this.isFlag() != state.isFlag()) {
               return false;
            }

            if (this.isFlag2() != state.isFlag2()) {
               return false;
            }

            Object object2 = this.getVec3d();
            Object object3 = state.getVec3d();
            return object2 == null ? object3 == null : object2.equals(object3);
         }
      }

      public boolean testObject(Object object) {
         return object instanceof MovementModel.State;
      }

      public MovementModel.State createState() {
         return new MovementModel.State(this.doubleValue, this.doubleValue2, this.vec3d, this.intValue, this.flag, this.flag2);
      }

      @Override
      public String toString() {
         return "Movement.State(speed="
            + this.getDoubleValue()
            + ", distance="
            + this.getDoubleValue2()
            + ", delta="
            + this.getVec3d()
            + ", stage="
            + this.getIntValue()
            + ", boost="
            + this.isFlag()
            + ", reset="
            + this.isFlag2()
            + ")";
      }

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public boolean isFlag() {
         return this.flag;
      }

      @Override
      public int hashCode() {
         int i = 59;
         int i2 = 1;
         long l = Double.doubleToLongBits(this.getDoubleValue());
         i2 = i2 * 59 + (int)(l >>> 32 ^ l);
         long l2 = Double.doubleToLongBits(this.getDoubleValue2());
         i2 = i2 * 59 + (int)(l2 >>> 32 ^ l2);
         i2 = i2 * 59 + this.getIntValue();
         i2 = i2 * 59 + (this.isFlag() ? 79 : 97);
         i2 = i2 * 59 + (this.isFlag2() ? 79 : 97);
         Object object = this.getVec3d();
         return i2 * 59 + (object == null ? 43 : object.hashCode());
      }
   }
}
