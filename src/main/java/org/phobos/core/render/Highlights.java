package org.phobos.core.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

public final class Highlights {
   public static final Map<Highlights.Highlight, Long> highlightMap = new HashMap<>();

   public static void run() {
      highlightMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
   }

   @Nullable
   public static Color resolveColor(Slot slot) {
      for (Highlights.Highlight highlight : highlightMap.keySet()) {
         if (highlight.slot().equals(slot)) {
            return highlight.color();
         }
      }

      return null;
   }

   public static void handleSlot(Slot slot, Color color, long l) {
      highlightMap.put(new Highlights.Highlight(slot, color), System.currentTimeMillis() + l);
   }

   public Highlights() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleBox(Box box, Color color, long l) {
      Render3d.handleCuboid(
         Render3d.Cuboid.createBuilder()
            .resolveBuilder11(box)
            .resolveBuilder18(ColorUtils.createColor6(color, 0.3F), color)
            .resolveBuilder12(1.0F)
            .resolveBuilder5(l)
            .getCuboid()
      );
   }

   public record Highlight(Slot slot, Color color) {
   }
}
