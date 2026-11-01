package org.phobos.core.movement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;
import org.phobos.core.entity.EntityUtils;

public class GroundMovementModel extends MovementModel {
   public double doubleValue2;
   public static final double doubleValue3 = 159.0;
   public static final double doubleValue4 = 0.01;
   public static final double doubleValue5 = 0.3888882626268;
   public static final double doubleValue6 = 0.66;

   public double computeFlag(boolean flag) {
      return flag ? 1.6835 : 1.395;
   }

   public double getDoubleValue7() {
      return 0.4;
   }

   @Override
   public MovementModel.State resolveState(PlayerEntity playerEntity, CollisionView collisionView, MovementModel.State state, Vec3d vec3d) {
      MovementModel.State state2 = state.createState();
      state2.setFlag2(false);
      if (!(playerEntity.fallDistance > 5.0)
         && !playerEntity.isSubmergedInWater()
         && !playerEntity.isInLava()
         && !playerEntity.isClimbing()
         && !EntityUtils.testLivingEntity2(playerEntity)) {
         double d = state2.doubleValue;
         double d2 = vec3d.y;
         if (state2.intValue == 1) {
            d = 1.35 * this.computePlayerEntity(playerEntity) - 0.01;
         } else if (state2.intValue == 2 && playerEntity.verticalCollision) {
            d2 = this.getDoubleValue7();
            d *= this.computeFlag(state2.flag);
            state2.flag = !state2.flag;
         } else if (state2.intValue == 3) {
            d = state2.doubleValue2 - 0.66 * (state2.doubleValue2 - this.computePlayerEntity(playerEntity));
         } else {
            if (playerEntity.verticalCollision
               || collisionView.getCollisions(playerEntity, playerEntity.getBoundingBox().offset(0.0, playerEntity.getVelocity().y, 0.0)).iterator().hasNext()) {
               state2.intValue = 1;
            }

            d = state2.doubleValue2 - state2.doubleValue2 / 159.0;
         }

         d = Math.max(d, this.computePlayerEntity(playerEntity));
         d += this.getDoubleValue2();
         state2.vec3d = new Vec3d(d * vec3d.x, d2, d * vec3d.z);
         state2.doubleValue = d;
         state2.intValue++;
         return state2;
      } else {
         state2.run();
         return state2;
      }
   }

   @Override
   public MovementModel.State resolveState2(PlayerEntity playerEntity, CollisionView collisionView, MovementModel.State state, Vec3d vec3d) {
      MovementModel.State state2 = state.createState();
      state2.setFlag2(false);
      if (testPlayerEntity(playerEntity)) {
         state2.run();
         return state2;
      } else {
         state2.doubleValue = this.computePlayerEntity(playerEntity) + this.getDoubleValue2();
         state2.vec3d = new Vec3d(state2.doubleValue * vec3d.x, vec3d.y, state2.doubleValue * vec3d.z);
         state2.intValue = 0;
         return state2;
      }
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }
}
