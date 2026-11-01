package org.phobos.core.chunk;

import java.util.function.Consumer;
import org.phobos.core.block.MutableBlockPos;

public final class ChunkTaskUtils {
   public static final int intValue = 3;

   public static void handleMutableBlockPos(MutableBlockPos mutableBlockPos, Consumer<MutableBlockPos> consumer) {
      int i4 = mutableBlockPos.getX();
      int i5 = mutableBlockPos.getY();
      int i6 = mutableBlockPos.getZ();

      for (int i3 = -3; i3 < 3; i3++) {
         for (int i2 = -3; i2 < 3; i2++) {
            for (int i = -3; i < 3; i++) {
               mutableBlockPos.set(i4 + i3, i5 + i, i6 + i2);
               consumer.accept(mutableBlockPos);
            }
         }
      }

      mutableBlockPos.set(i4, i5, i6);
   }

   public ChunkTaskUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
