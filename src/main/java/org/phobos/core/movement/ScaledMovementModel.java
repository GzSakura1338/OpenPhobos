package org.phobos.core.movement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CollisionView;
import org.phobos.core.entity.EntityUtils;

public class ScaledMovementModel extends GroundMovementModel {
   public final double doubleValue;

   public ScaledMovementModel(double d) {
      this.doubleValue = d;
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
         double d = this.doubleValue + this.getDoubleValue2();
         state2.vec3d = new Vec3d(d * vec3d.x, playerEntity.getVelocity().y, d * vec3d.z);
         state2.doubleValue = d;
         state2.intValue++;
         return state2;
      } else {
         state2.run();
         return state2;
      }
   }
}
