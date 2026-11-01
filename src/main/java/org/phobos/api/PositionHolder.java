package org.phobos.api;

import net.minecraft.util.math.Vec3i;

public interface PositionHolder {
   Vec3i getVec3i();

   default boolean testVec3i(Vec3i vec3i) {
      return this.getVec3i().equals(vec3i);
   }
}
