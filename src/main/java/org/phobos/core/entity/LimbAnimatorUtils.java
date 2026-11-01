package org.phobos.core.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LimbAnimator;

public final class LimbAnimatorUtils {
   public static void handleAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity, double[] doubles) {
      abstractClientPlayerEntity.capeX = doubles[0];
      abstractClientPlayerEntity.capeY = doubles[1];
      abstractClientPlayerEntity.capeZ = doubles[2];
   }

   public static void handleLimbAnimator(LimbAnimator limbAnimator, float[] floats) {
      limbAnimator.pos = floats[0];
      limbAnimator.prevSpeed = floats[1];
      limbAnimator.speed = floats[2];
   }

   public LimbAnimatorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static double[] values(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      return new double[]{abstractClientPlayerEntity.capeX, abstractClientPlayerEntity.capeY, abstractClientPlayerEntity.capeZ};
   }

   public static float[] values2(LimbAnimator limbAnimator) {
      return new float[]{limbAnimator.pos, limbAnimator.prevSpeed, limbAnimator.speed};
   }
}
