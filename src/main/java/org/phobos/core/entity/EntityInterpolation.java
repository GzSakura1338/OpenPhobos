package org.phobos.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class EntityInterpolation {
   public static Box resolveBox(Entity entity, float f) {
      Box box = entity.getBoundingBox();
      Vec3d vec3d = createVec3d(entity, f);
      return box.offset(-entity.getX(), -entity.getY(), -entity.getZ()).offset(vec3d);
   }

   public static Vec3d createVec3d(Entity entity, float f) {
      return new Vec3d(
         MathHelper.lerp(f, entity.prevX, entity.getX()), MathHelper.lerp(f, entity.prevY, entity.getY()), MathHelper.lerp(f, entity.prevZ, entity.getZ())
      );
   }

   public static Vec3d createVec3d2(Entity entity, float f) {
      return new Vec3d(
         MathHelper.lerp(f, entity.prevX, entity.getX()),
         MathHelper.lerp(f, entity.prevY, entity.getY()) + entity.getEyeHeight(entity.getPose()),
         MathHelper.lerp(f, entity.prevZ, entity.getZ())
      );
   }

   public static Box resolveBox2(Entity entity, Vec3d vec3d) {
      Box box = entity.getBoundingBox();
      return box.offset(-entity.getX(), -entity.getY(), -entity.getZ()).offset(vec3d);
   }

   public static Vec3d createVec3d3(Entity entity, double d, float f) {
      return new Vec3d(
         MathHelper.lerp(f, entity.prevX, entity.getX()), MathHelper.lerp(f, entity.prevY, entity.getY()) + d, MathHelper.lerp(f, entity.prevZ, entity.getZ())
      );
   }

   public static Vec3d createVec3d4(Entity entity, float f) {
      return new Vec3d(
         entity.getX() - MathHelper.lerp(f, entity.lastRenderX, entity.getX()),
         entity.getY() - MathHelper.lerp(f, entity.lastRenderY, entity.getY()),
         entity.getZ() - MathHelper.lerp(f, entity.lastRenderZ, entity.getZ())
      );
   }

   public static Vec3d createVec3d5(Entity entity, float f) {
      return new Vec3d(
         entity.getX() - MathHelper.lerp(f, entity.lastRenderX, entity.getX()),
         entity.getY() - MathHelper.lerp(f, entity.lastRenderY, entity.getY()),
         entity.getZ() - MathHelper.lerp(f, entity.lastRenderZ, entity.getZ())
      );
   }

   public static Vec3d createVec3d6(Entity entity, float f) {
      return new Vec3d(
         entity.prevX + (entity.getX() - entity.prevX) * f,
         entity.prevY + (entity.getY() - entity.prevY) * f,
         entity.prevZ + (entity.getZ() - entity.prevZ) * f
      );
   }

   public EntityInterpolation() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
