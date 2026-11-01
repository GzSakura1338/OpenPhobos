package org.phobos.core.movement;

public class StrictMovementModel extends GroundMovementModel {
   @Override
   public double computeFlag(boolean flag) {
      return 1.408;
   }

   @Override
   public double getDoubleValue7() {
      return 0.4000000054314141;
   }
}
