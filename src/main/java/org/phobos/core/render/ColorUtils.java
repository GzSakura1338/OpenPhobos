package org.phobos.core.render;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.util.UUID;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.phobos.module.client.ColorsModule;
import org.phobos.core.math.MathUtils;
import org.phobos.core.PhobosClient;

public final class ColorUtils {
   public static Color createColor(float f, float f2, float f3, float f4) {
      if (f2 < 0.0F || f2 > 100.0F) {
         throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
      } else if (f3 < 0.0F || f3 > 100.0F) {
         throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
      } else if (!(f4 < 0.0F) && !(f4 > 1.0F)) {
         f %= 360.0F;
         float var8;
         float f5 = f3 < 0.5 ? f3 * (1.0F + f2) : (f3 /= 100.0F) + (var8 = f2 / 100.0F) - var8 * f3;
         f2 = 2.0F * f3 - f5;
         float var7;
         f3 = Math.max(0.0F, computeFloat3(f2, f5, (var7 = f / 360.0F) + 0.33333334F));
         float f6 = Math.max(0.0F, computeFloat3(f2, f5, var7));
         f2 = Math.max(0.0F, computeFloat3(f2, f5, var7 - 0.33333334F));
         f3 = Math.min(f3, 1.0F);
         f6 = Math.min(f6, 1.0F);
         f2 = Math.min(f2, 1.0F);
         return new Color(f3, f6, f2, f4);
      } else {
         throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
      }
   }

   public static Color createColor2(Color color, float f) {
      return new Color(color.getRed() / 255.0F * f, color.getGreen() / 255.0F * f, color.getBlue() / 255.0F * f, color.getAlpha() / 255.0F);
   }

   public static Color createColor3(Color color, int i) {
      return new Color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, MathHelper.clamp(i / 255.0F, 0.0F, 1.0F));
   }

   public static Color createColor4(Color color, float f) {
      return new Color(
         color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, MathHelper.clamp(color.getAlpha() / 255.0F * f, 0.0F, 1.0F)
      );
   }

   public static int computeFloat(float f, Color color) {
      f = Math.abs(f);
      double d = System.currentTimeMillis() % 1750.0 / 850.0;
      float[] floats = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), floats);
      float f2 = (float)(floats[2] * Math.abs((f + d) % 1.0 - 0.55F) + 0.45F);
      return Color.HSBtoRGB(floats[0], floats[1], f2);
   }

   public static Color createColor5(Color color, int i) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathHelper.clamp(color.getAlpha() + i, 0, 255));
   }

   public static boolean testInt(int i) {
      return i >= 0 && i <= 255;
   }

   public static int computeColor(Color color) {
      return color.getAlpha() << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
   }

   public static Color resolveColor(Color color, float f) {
      float[] floats = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      return Color.getHSBColor(floats[0], floats[1], f);
   }

   public ColorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Color resolveColor2(PhobosClient phobosClient, UUID uUID, Color color, Color color2, Color color3) {
      if (phobosClient.getFriendList().testUUID(uUID)) {
         return color2;
      } else {
         return phobosClient.getEnemyList().testUUID(uUID) ? color3 : color;
      }
   }

   public static float computeDouble(double d) {
      return (float)(ColorsModule.floatValue - d * 0.001F);
   }

   public static Color createColor6(Color color, float f) {
      return new Color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, MathHelper.clamp(f, 0.0F, 1.0F));
   }

   public static Color resolveColor3(Color color, Color color2) {
      return createColor6(color2, color.getAlpha() / 255.0F);
   }

   public static Color createColor7(int i) {
      int i2 = i >> 24 & 0xFF;
      int i3 = i >> 16 & 0xFF;
      int i4 = i >> 8 & 0xFF;
      int i5 = i & 0xFF;
      return new Color(i3, i4, i5, i2);
   }

   public static Color createColor8(Color color, Color color2, float f) {
      float f2 = color.getRed() / 255.0F;
      float f3 = color.getGreen() / 255.0F;
      float f4 = color.getBlue() / 255.0F;
      float f5 = color.getAlpha() / 255.0F;
      float f6 = color2.getRed() / 255.0F;
      float f7 = color2.getGreen() / 255.0F;
      float f8 = color2.getBlue() / 255.0F;
      float f9 = color2.getAlpha() / 255.0F;
      float f10 = MathHelper.lerp(f, f2, f6);
      float f11 = MathHelper.lerp(f, f3, f7);
      float f12 = MathHelper.lerp(f, f4, f8);
      float f13 = MathHelper.lerp(f, f5, f9);
      return new Color(
         MathUtils.computeFloat3(f10, 0.0F, 1.0F),
         MathUtils.computeFloat3(f11, 0.0F, 1.0F),
         MathUtils.computeFloat3(f12, 0.0F, 1.0F),
         MathUtils.computeFloat3(f13, 0.0F, 1.0F)
      );
   }

   public static Vector3f createVector3f(Color color) {
      return new Vector3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
   }

   public static int[] values(int i) {
      int i2 = i >> 24 & 0xFF;
      int i3 = i >> 16 & 0xFF;
      int i4 = i >> 8 & 0xFF;
      int i5 = i & 0xFF;
      return new int[]{i3, i4, i5, i2};
   }

   public static Color resolveColor4(Color color) {
      return createColor6(new Color((color.getRGB() & 16579836) >> 2 | color.getRGB() & -202116109), color.getAlpha() / 255.0F);
   }

   public static int computeFloat2(float f, Color color, float f2, float f3, float f4) {
      f = Math.abs(f);
      double d = 1.0 / (1.0F - f2);
      double d2 = System.currentTimeMillis() % (1750.0 * d) / (850.0 * d);
      float[] floats = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), floats);
      float f5 = (float)(f4 * Math.abs((f + d2) % 1.0 - 0.55F) + 0.45F);
      return Color.HSBtoRGB(floats[0], f3, f5);
   }

   public static Color createColor9(Color color, Color color2, float f) {
      if (f > 1.0F) {
         f = 1.0F;
      } else if (f < 0.0F) {
         f = 0.0F;
      }

      float f2 = 1.0F - f;
      int i = color.getRGB();
      int i2 = color2.getRGB();
      int i3 = i >> 24 & 0xFF;
      int i4 = (i & 0xFF0000) >> 16;
      int i5 = (i & 0xFF00) >> 8;
      int i6 = i & 0xFF;
      int i7 = i2 >> 24 & 0xFF;
      int i8 = (i2 & 0xFF0000) >> 16;
      int i9 = (i2 & 0xFF00) >> 8;
      int i10 = i2 & 0xFF;
      int i11 = (int)(i3 * f2 + i7 * f);
      int i12 = (int)(i4 * f2 + i8 * f);
      int i13 = (int)(i5 * f2 + i9 * f);
      int i14 = (int)(i6 * f2 + i10 * f);
      return new Color(i11 << 24 | i12 << 16 | i13 << 8 | i14);
   }

   public static Color resolveColor5(int i, int i2, float f, float f2) {
      float f3 = (float)((System.currentTimeMillis() + i2) % i);
      f3 /= i;
      return Color.getHSBColor(f3, f, f2);
   }

   public static Color resolveColor6(Color color, float f) {
      float[] floats = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      return Color.getHSBColor(floats[0], f, floats[2]);
   }

   public static Vec3d createVec3d(Color color) {
      return new Vec3d(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
   }

   public static float[] values2(int[] ints) {
      Preconditions.checkArgument(ints.length == 4, "Expected int[] of size 4, got " + ints.length);

      for (int i = 0; i < ints.length; i++) {
         Preconditions.checkArgument(testInt(ints[i]), "Expected in[" + i + "] to be 0-255, got " + ints[i]);
      }

      return new float[]{ints[0] / 255.0F, ints[1] / 255.0F, ints[2] / 255.0F, ints[3] / 255.0F};
   }

   public static float computeFloat3(float f, float f2, float f3) {
      if (f3 < 0.0F) {
         f3++;
      }

      if (f3 > 1.0F) {
         f3--;
      }

      if (6.0F * f3 < 1.0F) {
         return f + (f2 - f) * 6.0F * f3;
      } else if (2.0F * f3 < 1.0F) {
         return f2;
      } else {
         return 3.0F * f3 < 2.0F ? f + (f2 - f) * 6.0F * (0.6666667F - f3) : f;
      }
   }

   public static float[] values3(float[] floats, float f) {
      return new float[]{floats[0], floats[1], floats[2], f};
   }

   public static Vector4f createVector4f(Color color) {
      return new Vector4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }

   public static float[] values4(Color color) {
      return new float[]{color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F};
   }

   public static int[] values5(int i) {
      int i2 = i >> 16 & 0xFF;
      int i3 = i >> 8 & 0xFF;
      int i4 = i & 0xFF;
      return new int[]{i2, i3, i4};
   }

   public static Color resolveColor7(Color color, Color color2, float f, float f2) {
      return createColor6(createColor9(color, color2, f), f2);
   }
}
