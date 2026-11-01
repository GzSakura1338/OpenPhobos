package org.phobos.core.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class VectorMath {
   public static double computeVec3d(Vec3d vec3d, Vec3d vec3d2) {
      return computeVec3d3(vec3d, vec3d2) * 20.0;
   }

   public static List<Vec3d> resolveList(List<Vec3d> list) {
      List<Vec3d> list2 = new ArrayList<>(list.size() - 1);

      for (int i = 0; i < list.size() - 1; i++) {
         list2.add(list.get(i + 1).subtract(list.get(i)));
      }

      return list2;
   }

   public static double computeFloat(float f, float f2, float f3) {
      if (f < 0.0F) {
         f3 += 180.0F;
      }

      float f4 = 1.0F;
      if (f < 0.0F) {
         f4 = -0.5F;
      } else if (f > 0.0F) {
         f4 = 0.5F;
      }

      if (f2 > 0.0F) {
         f3 -= 70.0F * f4;
      }

      if (f2 < 0.0F) {
         f3 += 70.0F * f4;
      }

      return Math.toRadians(f3);
   }

   public static boolean testVec3d(Vec3d vec3d) {
      return vec3d.x != 0.0 || vec3d.z != 0.0;
   }

   public VectorMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static double computeVec3d2(Vec3d vec3d, Vec3d vec3d2) {
      return Math.max(Math.abs(vec3d.x - vec3d2.x), Math.abs(vec3d.z - vec3d2.z));
   }

   public static Vec3d createVec3d(Vec3d vec3d) {
      return new Vec3d(vec3d.x, 0.0, vec3d.z);
   }

   public static double computeDouble(double d, int i) {
      if (i == 0) {
         return d;
      }

      double d2 = d;

      for (int i2 = 0; i2 < i; i2++) {
         d2 = (d2 - 0.08) * 0.98F;
      }

      return d2;
   }

   public static Vec3d createVec3d2(double d, double d2, double d3) {
      return new Vec3d(-MathHelper.sin((float)d2) * d, d3, MathHelper.cos((float)d2) * d);
   }

   public static boolean testVec3d2(Vec3d vec3d) {
      return Math.abs(vec3d.x) >= 3.9 || Math.abs(vec3d.y) >= 3.9 || Math.abs(vec3d.z) >= 3.9;
   }

   public static double computePlayerEntity(PlayerEntity playerEntity) {
      Vec3d vec3d = playerEntity.getVelocity();
      return Math.hypot(vec3d.x, vec3d.z);
   }

   public static double computeAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      try {
         int i = ((StatusEffectInstance)Objects.requireNonNull(
                  (StatusEffectInstance)abstractClientPlayerEntity.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST)
               ))
               .getAmplifier()
            + 1;
         return 0.04837 * i + 0.5356 * i + 1.252;
      } catch (NullPointerException nullPointerException) {
         return 1.252203340253729;
      }
   }

   public static double computeVec3d3(Vec3d vec3d, Vec3d vec3d2) {
      return createVec3d(vec3d2).distanceTo(createVec3d(vec3d));
   }
}
