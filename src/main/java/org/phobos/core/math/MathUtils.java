package org.phobos.core.math;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.MixinClassNames;

public final class MathUtils {
   public static final Random random = new Random();

   public static float computeFloat(float f) {
      return random.nextFloat() * f;
   }

   public static int value() {
      return random.nextInt();
   }

   public static float value2() {
      return random.nextFloat();
   }

   public static double computeDouble(double d, int i) {
      return computeDouble2(d, i, RoundingMode.HALF_UP);
   }

   public static <N extends Number> N resolveNumber(N number, N number2) {
      if (number instanceof Integer) {
         return MixinClassNames.CCS.resolveObject2(computeInt5(number.intValue(), number2.intValue()), number);
      } else if (number instanceof Float) {
         return MixinClassNames.CCS.resolveObject2(computeFloat8(number.floatValue(), number2.floatValue()), number);
      } else if (number instanceof Double) {
         return MixinClassNames.CCS.resolveObject2(computeDouble9(number.doubleValue(), number2.doubleValue()), number);
      } else if (number instanceof Long) {
         return MixinClassNames.CCS.resolveObject2(computeLong5(number.longValue(), number2.longValue()), number);
      } else if (number instanceof Short) {
         return MixinClassNames.CCS.resolveObject2(computeInt5(number.shortValue(), number2.shortValue()), number);
      } else if (number instanceof Byte) {
         return MixinClassNames.CCS.resolveObject2(computeInt5(number.byteValue(), number2.byteValue()), number);
      } else {
         throw new UnsupportedOperationException("Unable to get random from type %s".formatted(number.getClass().getSimpleName()));
      }
   }

   public static double computeDouble2(double d, int i, RoundingMode roundingMode) {
      Preconditions.checkArgument(i >= 0, "Rounding with less than 0 places is not allowed!");
      return new BigDecimal(Double.toString(d)).setScale(i, roundingMode).doubleValue();
   }

   public static float computeFloat2(float f, int i, RoundingMode roundingMode) {
      Preconditions.checkArgument(i >= 0, "Rounding with less than 0 places is not allowed!");
      return !Float.isFinite(f) ? f : new BigDecimal(Float.toString(f)).setScale(i, roundingMode).floatValue();
   }

   public static int computeInt(int i) {
      return computeInt5(-i, i);
   }

   public static long computeLong(long l) {
      return random.nextLong() % l;
   }

   public static double computeDouble3(double d) {
      return computeDouble9(-d, d);
   }

   public static double computeDouble4(double d) {
      return random.nextDouble() * d;
   }

   public static float computeFloat3(float f, float f2, float f3) {
      return f > f3 ? f3 : Math.max(f, f2);
   }

   public static long computeLong2(long l, long l2, long l3) {
      return l > l3 ? l3 : Math.max(l, l2);
   }

   public static int computeInt2(int i) {
      return random.nextInt(i);
   }

   public static double computeDouble5(double d, double d2, double d3) {
      return d > d3 ? d3 : Math.max(d, d2);
   }

   public static long computeLong3(long l, long l2, long l3) {
      return l < l2 ? l2 : Math.min(l, l3);
   }

   public static int computeInt3(int i, int i2, int i3) {
      return i > i3 ? i3 : Math.max(i, i2);
   }

   public static double computeDouble6(double d, double d2, double d3) {
      return d < d2 ? Math.max(d, d2) : (d > d3 ? Math.min(d, d3) : d);
   }

   public static long value3() {
      return random.nextLong();
   }

   public static int computeInt4(int i, int i2, int i3) {
      return i < i2 ? i2 : Math.min(i, i3);
   }

   public static float computeFloat4(float f, float f2, float f3) {
      return f < f2 ? Math.max(f, f2) : (f > f3 ? Math.min(f, f3) : f);
   }

   public static <N extends Number> N resolveNumber2(N number) {
      return (N)(switch (number) {
         case Integer integer -> (Number)0;
         case Float floatValue -> (Number)0.0F;
         case Double doubleValue -> (Number)0.0;
         case Long longValue -> (Number)0L;
         default -> throw new UnsupportedOperationException("Invalid type");
      });
   }

   public static double value4() {
      return random.nextDouble();
   }

   public static int computeInt5(int i, int i2) {
      return random.nextInt(i, i2);
   }

   public static float computeFloat5(float f, int i) {
      return computeFloat2(f, i, RoundingMode.HALF_UP);
   }

   public static double computeDouble7(double d, double d2, double d3) {
      return (d - d2) / (d3 - d2);
   }

   public static boolean testSupplier(Supplier<Float> supplier) {
      Float floatValue = supplier.get();
      return floatValue.equals(100.0F) || Math.random() * 100.0 <= floatValue.floatValue();
   }

   public static Vec3d createVec3d(Vec3d vec3d, double d) {
      return new Vec3d(vec3d.getX(), vec3d.getY(), d);
   }

   public static float computeFloat6(float f) {
      return (float)computeDouble11(f);
   }

   public static float computeFloat7(float f, float f2, float f3) {
      return (f - f2) / (f3 - f2);
   }

   public static Vec3d createVec3d2(Vec3d vec3d, double d) {
      return new Vec3d(d, vec3d.getY(), vec3d.getZ());
   }

   public static double computeVec3d(Vec3d vec3d, Vec3d vec3d2) {
      double d = vec3d.length() * vec3d2.length();
      if (d < 1.0E-4) {
         return 0.0;
      } else {
         double d2 = vec3d.dotProduct(vec3d2);
         double d3 = d2 / d;
         if (d3 > 1.0) {
            return 0.0;
         } else {
            return d3 < -1.0 ? 180.0 : Math.acos(d3) * 180.0 / Math.PI;
         }
      }
   }

   public static double computeDouble8(double d, double d2, double d3, double d4, double d5, double d6) {
      double d7 = d4 - d;
      double d8 = d5 - d2;
      double d9 = d6 - d3;
      return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
   }

   public static float computeFloat8(float f, float f2) {
      return (float)(random.nextDouble() * (f2 - f) + f);
   }

   public static Vec3d createVec3d3(Vec3d vec3d, double d) {
      return new Vec3d(vec3d.getX(), d, vec3d.getZ());
   }

   public MathUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static long computeLong4(long l) {
      return computeLong5(-l, l);
   }

   public static boolean testSupplier2(Supplier<Double> supplier) {
      Double doubleValue = supplier.get();
      return doubleValue.equals(100.0) || Math.random() * 100.0 <= doubleValue;
   }

   public static float computeFloat9(float f) {
      return computeFloat8(-f, f);
   }

   public static double computeDouble9(double d, double d2) {
      return random.nextDouble() * (d2 - d) + d;
   }

   public static boolean testFloat(float f, float f2, float f3) {
      return Math.abs(f - f2) < f3;
   }

   public static Vec3d createVec3d4(float f, float f2) {
      float f3 = MathHelper.cos(-f * (float) (Math.PI / 180.0));
      float f4 = MathHelper.sin(-f * (float) (Math.PI / 180.0));
      float f5 = MathHelper.cos(f2 * (float) (Math.PI / 180.0));
      float f6 = MathHelper.sin(f2 * (float) (Math.PI / 180.0));
      return new Vec3d(f4 * f5, -f6, f3 * f5);
   }

   public static double computeDouble10(double d, double d2, double d3, double d4) {
      double d5 = d3 - d;
      double d6 = d4 - d2;
      return Math.sqrt(d5 * d5 + d6 * d6);
   }

   public static double computeDouble11(double d) {
      if (!Double.isNaN(d) && !Double.isInfinite(d)) {
         return d > 0.0 ? Math.floor(d) : Math.ceil(d);
      } else {
         return d;
      }
   }

   public static double computeDouble12(double d, double d2, double d3, double d4) {
      return Math.sqrt(computeDouble10(d, d2, d3, d4));
   }

   public static boolean testDouble(double d, double d2, double d3) {
      return Math.abs(d - d2) < d3;
   }

   public static double computeDouble13(double d, double d2, double d3, double d4, double d5, double d6) {
      return Math.sqrt(computeDouble8(d, d2, d3, d4, d5, d6));
   }

   public static long computeLong5(long l, long l2) {
      return (long)(random.nextDouble() * (l2 - l) + l);
   }
}
