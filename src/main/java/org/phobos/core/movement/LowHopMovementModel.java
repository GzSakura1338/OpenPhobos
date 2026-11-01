package org.phobos.core.movement;

public class LowHopMovementModel extends GroundMovementModel {
   @Override
   public double getDoubleValue7() {
      return 0.4F;
   }

   @Override
   public double getDoubleValue() {
      return 0.287;
   }

   @Override
   public double computeFlag(boolean flag) {
      return 1.535;
   }
}
