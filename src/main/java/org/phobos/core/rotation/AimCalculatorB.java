package org.phobos.core.rotation;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.phobos.api.RotationCalculator;

public class AimCalculatorB implements RotationCalculator {
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

   @Override
   public float[] values(ClientPlayerEntity clientPlayerEntity, Entity entity) {
      float f = (clientPlayerEntity.getActiveItem().getMaxUseTime(clientPlayerEntity) - clientPlayerEntity.getItemUseTime()) / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if (f >= 1.0F) {
         f = 1.0F;
      }

      double d = f * 3.0F;
      double d2 = 0.05F;
      float f2 = (float)(-Math.toDegrees(this.computeClientPlayerEntity(clientPlayerEntity, entity, d, d2)));
      double d3 = entity.getX() - entity.prevX;
      double d4 = entity.getZ() - entity.prevZ;
      double d5 = clientPlayerEntity.distanceTo(entity);
      d5 -= d5 % 2.0;
      d3 = d5 / 2.0 * d3 * (clientPlayerEntity.isSprinting() ? 1.3 : 1.1);
      d4 = d5 / 2.0 * d4 * (clientPlayerEntity.isSprinting() ? 1.3 : 1.1);
      float f3 = (float)Math.toDegrees(Math.atan2(entity.getZ() + d4 - clientPlayerEntity.getZ(), entity.getX() + d3 - clientPlayerEntity.getX())) - 90.0F;
      return new float[]{f3, f2};
   }

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Entity entity, double d, double d2) {
      double d3 = entity.getY() + entity.getStandingEyeHeight() / 2.0F - (clientPlayerEntity.getY() + clientPlayerEntity.getStandingEyeHeight());
      double d4 = entity.getX() - clientPlayerEntity.getX();
      double d5 = entity.getZ() - clientPlayerEntity.getZ();
      double d6 = Math.sqrt(d4 * d4 + d5 * d5);
      return this.computeDouble(d, d2, d6, d3);
   }
}
