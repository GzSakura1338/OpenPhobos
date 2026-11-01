package org.phobos.api;

import net.minecraft.util.math.Vec3i;

public interface HoleScanner {
   Vec3i[] vec3iArray = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(1, 0, 0),
      new Vec3i(0, -1, 0),
      new Vec3i(1, -1, 0),
      new Vec3i(0, 1, 0),
      new Vec3i(1, 1, 0),
      new Vec3i(0, 2, 0),
      new Vec3i(1, 2, 0),
      new Vec3i(-1, 0, 0),
      new Vec3i(2, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(0, 0, -1),
      new Vec3i(1, 0, 1),
      new Vec3i(1, 0, -1)
   };
   Vec3i[] vec3iArray2 = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(0, -1, 0),
      new Vec3i(0, 1, 0),
      new Vec3i(0, 2, 0),
      new Vec3i(1, 0, 0),
      new Vec3i(-1, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(0, 0, -1)
   };
   Vec3i[] vec3iArray3 = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(0, -1, 0),
      new Vec3i(0, -1, 1),
      new Vec3i(0, 1, 0),
      new Vec3i(0, 1, 1),
      new Vec3i(0, 2, 0),
      new Vec3i(0, 2, 1),
      new Vec3i(0, 0, -1),
      new Vec3i(0, 0, 2),
      new Vec3i(1, 0, 0),
      new Vec3i(-1, 0, 0),
      new Vec3i(1, 0, 1),
      new Vec3i(-1, 0, 1)
   };
   Vec3i[] vec3iArray4 = new Vec3i[]{
      new Vec3i(0, 0, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, -1), new Vec3i(0, 1, 0), new Vec3i(0, 2, 0)
   };
   Vec3i[] vec3iArray5 = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(1, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(1, 0, 1),
      new Vec3i(0, -1, 0),
      new Vec3i(1, -1, 0),
      new Vec3i(0, -1, 1),
      new Vec3i(1, -1, 1),
      new Vec3i(0, 1, 0),
      new Vec3i(1, 1, 0),
      new Vec3i(0, 1, 1),
      new Vec3i(1, 1, 1),
      new Vec3i(0, 2, 0),
      new Vec3i(1, 2, 0),
      new Vec3i(0, 2, 1),
      new Vec3i(1, 2, 1),
      new Vec3i(-1, 0, 0),
      new Vec3i(0, 0, -1),
      new Vec3i(-1, 0, 1),
      new Vec3i(1, 0, -1),
      new Vec3i(2, 0, 0),
      new Vec3i(0, 0, 2),
      new Vec3i(2, 0, 1),
      new Vec3i(1, 0, 2)
   };
}
