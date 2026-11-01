package org.phobos.core.util;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;

public final class BufferUtils {
   public static void handleBufferBuilder(BufferBuilder bufferBuilder) {
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
   }

   public BufferUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
