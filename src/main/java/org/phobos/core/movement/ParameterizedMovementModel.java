package org.phobos.core.movement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;
import org.phobos.core.entity.EntityUtils;

public class ParameterizedMovementModel extends GroundMovementModel {
   public final double doubleValue;
   public final double doubleValue7;
   public final boolean flag;

   public ParameterizedMovementModel(double d, double d2, boolean flag) {
      this.doubleValue = d;
      this.doubleValue7 = d2;
      this.flag = flag;
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
         double d = vec3d.getY();
         if (this.flag && playerEntity.verticalCollision && playerEntity.isOnGround()) {
            d = this.getDoubleValue7();
         }

         double d2 = this.computePlayerEntity(playerEntity) + this.getDoubleValue2();
         state2.vec3d = new Vec3d(d2 * vec3d.x, d, d2 * vec3d.z);
         state2.doubleValue = d2;
         state2.intValue++;
         return state2;
      } else {
         state2.run();
         return state2;
      }
   }

   @Override
   public double getDoubleValue() {
      return this.doubleValue;
   }

   @Override
   public double getDoubleValue7() {
      return this.doubleValue7;
   }

   public ParameterizedMovementModel(double d, double d2) {
      this(d, d2, true);
   }
}
