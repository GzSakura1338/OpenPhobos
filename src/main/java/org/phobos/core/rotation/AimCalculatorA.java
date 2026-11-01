package org.phobos.core.rotation;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.phobos.api.RotationCalculator;

public class AimCalculatorA implements RotationCalculator {
   @Override
   public float[] values(ClientPlayerEntity clientPlayerEntity, Entity entity) {
      double d = 1.5;
      double d2 = 0.03;
      double d3 = entity.getX() - entity.prevX;
      double d4 = entity.getZ() - entity.prevZ;
      double d5 = clientPlayerEntity.distanceTo(entity);
      d5 -= d5 % 2.0;
      d3 = d5 / 2.0 * d3 * (clientPlayerEntity.isSprinting() ? 1.3 : 1.1);
      d4 = d5 / 2.0 * d4 * (clientPlayerEntity.isSprinting() ? 1.3 : 1.1);
      double d6 = entity.getX() + d3;
      double d7 = entity.getZ() + d4;
      double d8 = entity.getY() + entity.getStandingEyeHeight() / 2.0F - (clientPlayerEntity.getY() + clientPlayerEntity.getStandingEyeHeight());
      double d9 = d6 - clientPlayerEntity.getX();
      double d10 = d7 - clientPlayerEntity.getZ();
      double d11 = Math.sqrt(d9 * d9 + d10 * d10);
      float f = (float)(-Math.toDegrees(this.computeDouble(d, d2, d11, d8)));
      float f2 = (float)Math.toDegrees(Math.atan2(d10, d9)) - 90.0F;
      return new float[]{f2, f};
   }

   public float computeDouble(double d, double d2, double d3, double d4) {
      double d5 = d2 * (d3 * d3);
      d4 = 2.0 * d4 * (d * d);
      d4 = d2 * (d5 + d4);
      d4 = Math.sqrt(d * d * d * d - d4);
      d = d * d - d4;
      d4 = Math.atan2(d * d + d4, d2 * d3);
      d = Math.atan2(d, d2 * d3);
      return (float)Math.min(d4, d);
   }
}
