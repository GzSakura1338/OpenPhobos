package org.phobos.core.placement;

import net.minecraft.util.math.Vec3i;
import org.phobos.api.PositionHolder;

public class PositionRef implements PositionHolder {
   public final Vec3i vec3i;

   public PositionRef(Vec3i vec3i) {
      this.vec3i = vec3i;
   }

   @Override
   public Vec3i getVec3i() {
      return this.vec3i;
   }
}
