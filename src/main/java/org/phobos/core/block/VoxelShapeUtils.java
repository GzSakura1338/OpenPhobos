package org.phobos.core.block;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public final class VoxelShapeUtils {
   public static VoxelShape resolveVoxelShape(VoxelShape voxelShape, double d, double d2, double d3) {
      if (voxelShape.isEmpty()) {
         return VoxelShapes.empty();
      }

      if (voxelShape == VoxelShapes.fullCube()) {
         return VoxelShapes.cuboid(d, d2, d3, 1.0 - d, 1.0 - d2, 1.0 - d3);
      }

      AtomicReference<VoxelShape> atomicReference = new AtomicReference<>(VoxelShapes.empty());
      voxelShape.forEachBox((d4, d5, d6, d7, d8, d9) -> {
         double d13 = d7 - d4;
         double d14 = d8 - d5;
         double d15 = d9 - d6;
         boolean flag = d == 0.0 || d13 > d * 2.0;
         boolean flag2 = d2 == 0.0 || d14 > d2 * 2.0;
         boolean flag3 = d3 == 0.0 || d15 > d3 * 2.0;
         if (flag && flag2 && flag3) {
            double d10 = Math.max(d, 0.0);
            double d11 = Math.max(d2, 0.0);
            double d12 = Math.max(d3, 0.0);
            VoxelShape voxelShapex = VoxelShapes.cuboid(d4 + d10, d5 + d11, d6 + d12, d7 - d10, d8 - d11, d9 - d12);
            atomicReference.set(VoxelShapes.combine(atomicReference.get(), voxelShapex, BooleanBiFunction.OR));
         }
      });
      return atomicReference.get();
   }

   public VoxelShapeUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
