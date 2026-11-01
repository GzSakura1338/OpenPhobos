package org.phobos.core.prediction;

import net.minecraft.util.math.Vec3d;
import org.phobos.core.movement.MovementModel;
import org.phobos.core.fakeplayer.TrackedFakePlayer;

public class PredictionPosition {
   public Vec3d vec3d;
   public final TrackedEntityState trackedEntityState;
   public final MovementModel movementModel;

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public PredictionPosition(MovementModel movementModel, TrackedEntityState trackedEntityState) {
      this.movementModel = movementModel;
      this.trackedEntityState = trackedEntityState;
   }

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public TrackedEntityState getTrackedEntityState() {
      return this.trackedEntityState;
   }

   public void run() {
      this.vec3d = null;
   }

   public double computeTrackedFakePlayer(TrackedFakePlayer trackedFakePlayer) {
      return trackedFakePlayer.isOnGround()
         ? (this.trackedEntityState.isFlag() ? -10.0 : -this.movementModel.value3() * this.movementModel.value4())
         : (trackedFakePlayer.getVelocity().getY() - this.movementModel.value3()) * this.movementModel.value4();
   }

   public void handleVec3d(
      Vec3d vec3d,
      TrackedFakePlayer trackedFakePlayer,
      TrackedFakePlayer trackedFakePlayer2,
      PlayerMovementTracker playerMovementTracker,
      boolean flag
   ) {
      this.vec3d = new Vec3d(vec3d.x, this.computeTrackedFakePlayer(trackedFakePlayer), vec3d.z);
      if (flag && playerMovementTracker.getFlagPair().isCompleted()) {
         FlagGroup2 flagGroup2 = playerMovementTracker.getFlagPair().getFlagGroup2();
         if (trackedFakePlayer.isOnGround() && !trackedFakePlayer.getFlagHolder().isFlag() && flagGroup2.isFlag4()) {
            this.vec3d = new Vec3d(this.vec3d.getX(), 0.42, this.vec3d.getZ());
            trackedFakePlayer.getFlagHolder().setFlag(true);
         } else {
            if (flagGroup2.isFlag() && flagGroup2.isFlag2()) {
               this.vec3d = new Vec3d(this.vec3d.getX(), -(this.movementModel.value3() + this.movementModel.value2()), this.vec3d.getZ());
            }
         }
      }
   }

   public MovementModel getMovementModel() {
      return this.movementModel;
   }
}
