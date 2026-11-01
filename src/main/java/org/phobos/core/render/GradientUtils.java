package org.phobos.core.render;

import java.awt.Color;
import org.phobos.api.Transformation;
import org.phobos.core.math.MathUtils;

public final class GradientUtils {
   public static int computeDouble(double d, double d2) {
      return (int)((System.currentTimeMillis() / d + d2) % 360.0);
   }

   public static Color resolveColor(double d, double d2, Color color, Color color2, Color color3, Color color4) {
      int i = (int)((System.currentTimeMillis() / d + d2) % 360.0);
      float f = i / 360.0F;
      if (f < 0.25F) {
         return resolveColor4(color, color2, f * 4.0F);
      } else if (f < 0.5F) {
         return resolveColor4(color2, color3, (f - 0.25F) * 4.0F);
      } else {
         return f < 0.75F ? resolveColor4(color3, color4, (f - 0.5F) * 4.0F) : resolveColor4(color4, color, (f - 0.75F) * 4.0F);
      }
   }

   @Transformation("colors.length = 2...4")
   public static Color resolveColor2(double d, double d2, Color... colors) {
      if (colors == null) {
         throw new IllegalArgumentException();
      }

      return switch (colors.length) {
         case 2 -> resolveColor5(d, d2, colors[0], colors[1]);
         case 3 -> resolveColor3(d, d2, colors[0], colors[1], colors[2]);
         case 4 -> resolveColor(d, d2, colors[0], colors[1], colors[2], colors[3]);
         default -> throw new IllegalArgumentException();
      };
   }

   public static Color resolveColor3(double d, double d2, Color color, Color color2, Color color3) {
      int i = (int)((System.currentTimeMillis() / d + d2) % 360.0);
      float f = i / 360.0F;
      if (f < 0.33333334F) {
         return resolveColor4(color, color2, f * 3.0F);
      } else {
         return f < 0.6666667F ? resolveColor4(color2, color3, (f - 0.33333334F) * 3.0F) : resolveColor4(color3, color, (f - 0.6666667F) * 3.0F);
      }
   }

   public GradientUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Color resolveColor4(Color color, Color color2, float f) {
      return ColorUtils.createColor8(color, color2, MathUtils.computeFloat3(f, 0.0F, 1.0F));
   }

   public static Color resolveColor5(double d, double d2, Color color, Color color2) {
      int i = computeDouble(d, d2);
      int i2 = (i >= 180 ? 360 - i : i) * 2;
      return resolveColor4(color, color2, i2 / 360.0F);
   }
}
