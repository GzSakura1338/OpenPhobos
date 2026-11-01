package org.phobos.core.gui;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class HoverRegion {
   public static final HoverRegion INSTANCE = new HoverRegion(Set.of());
   public final Set<ClipRect> clipRectSet;

   public boolean testDouble(double d, double d2) {
      if (this.clipRectSet.isEmpty()) {
         return false;
      }

      for (ClipRect clipRect : this.clipRectSet) {
         if (clipRect.testDouble(d, d2)) {
            return true;
         }
      }

      return false;
   }

   public static HoverRegion getINSTANCE() {
      return INSTANCE;
   }

   public static HoverRegion resolveHoverRegion(Function<HoverRegion, HoverRegion> function) {
      HoverRegion hoverRegion = new HoverRegion(new HashSet<>());
      return function.apply(hoverRegion);
   }

   public Set<ClipRect> getClipRectSet() {
      return this.clipRectSet;
   }

   public HoverRegion(Set<ClipRect> set) {
      this.clipRectSet = set;
   }

   public static HoverRegion createHoverRegion(float f, float f2, float f3, float f4) {
      return new HoverRegion(new HashSet<>(Set.of(ClipRect.createClipRect(f, f2, f3, f4))));
   }
}
