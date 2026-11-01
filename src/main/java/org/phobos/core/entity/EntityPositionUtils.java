package org.phobos.core.entity;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public final class EntityPositionUtils {
   public static Vec3d resolveVec3d(Entity entity, Vec3d vec3d, float f) {
      Box box3 = entity.getBoundingBox();
      List<VoxelShape> list2 = entity.getWorld().getEntityCollisions(entity, box3.stretch(vec3d));
      Vec3d vec3d3 = vec3d.lengthSquared() == 0.0 ? vec3d : Entity.adjustMovementForCollisions(entity, vec3d, box3, entity.getWorld(), list2);
      boolean flag = vec3d.x != vec3d3.x;
      boolean flag2 = vec3d.y != vec3d3.y;
      boolean flag3 = vec3d.z != vec3d3.z;
      boolean flag4 = flag2 && vec3d.y < 0.0;
      if (f > 0.0F && (flag4 || entity.isOnGround()) && (flag || flag3)) {
         Box box = flag4 ? box3.offset(0.0, vec3d3.y, 0.0) : box3;
         Box box2 = box.stretch(vec3d.x, f, vec3d.z);
         if (!flag4) {
            box2 = box2.stretch(0.0, -1.0E-5F, 0.0);
         }

         List<VoxelShape> list = Entity.findCollisionsForMovement(entity, entity.getWorld(), list2, box2);
         float f3 = (float)vec3d3.y;
         float[] floats = Entity.collectStepHeights(box, list, f, f3);

         for (float f2 : floats) {
            Vec3d vec3d2 = Entity.adjustMovementForCollisions(new Vec3d(vec3d.x, f2, vec3d.z), box, list);
            if (vec3d2.horizontalLengthSquared() > vec3d3.horizontalLengthSquared()) {
               double d = box3.minY - box.minY;
               return vec3d2.add(0.0, -d, 0.0);
            }
         }
      }

      return vec3d3;
   }

   public EntityPositionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
