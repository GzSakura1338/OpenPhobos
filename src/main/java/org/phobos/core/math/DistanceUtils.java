package org.phobos.core.math;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public final class DistanceUtils {
   public static double computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Entity entity) {
      return Math.sqrt(entity.getBoundingBox().squaredMagnitude(clientPlayerEntity.getEyePos()));
   }

   public DistanceUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
