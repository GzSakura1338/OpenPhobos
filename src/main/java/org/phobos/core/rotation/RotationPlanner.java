package org.phobos.core.rotation;

import lombok.SneakyThrows;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.core.math.MutableVec3d;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.render.Render3d;

public class RotationPlanner implements MinecraftHolder {
   public boolean flag;
   public double doubleValue;
   public boolean flag2;
   public double doubleValue2;
   public double doubleValue3;
   public double doubleValue4;
   public double doubleValue5;
   public Pair<Float, Float> floatValuePair;
   public boolean flag3;
   public double doubleValue6;
   public double doubleValue7;
   public double doubleValue8 = 0.0;
   public long longValue;
   public final List<Box> boxList;
   public boolean flag4;
   public double doubleValue9;
   public int intValue;
   public long longValue2;
   public Vec3d vec3d;

   public static float computeDouble(double d, double d2, double d3) {
      return RotationUtils.computeFloat(computeFloat((float)d, (float)d2, (float)d3));
   }

   public void handleVec3d(Vec3d vec3d, Color color) {
      Render3d.handleCuboid(
         Render3d.Cuboid.createBuilder()
            .resolveBuilder11(new Box(vec3d, vec3d).expand(0.03))
            .resolveBuilder18(ColorUtils.createColor6(color, 0.3F), color)
            .resolveBuilder12(1.0F)
            .resolveBuilder5(20000L)
            .getCuboid()
      );
   }

   public static float computeFloat(float f, float f2, float f3) {
      float f4;
      if (f > f2) {
         float f5 = f - f2;
         float f6 = f2 + 360.0F - f;
         if (f5 > f6) {
            f4 = -f2 - 360.0F + f;
         } else {
            f4 = f5;
         }
      } else {
         if (!(f < f2)) {
            return f2;
         }

         float f9 = f2 - f;
         float f10 = f + 360.0F - f2;
         if (f9 > f10) {
            f4 = f2 + 360.0F + f;
         } else {
            f4 = -f9;
         }
      }

      f4 = MathHelper.wrapDegrees(f4);
      if (Math.abs(f4) < 0.1 * Math.random() + 0.1) {
         return f2;
      } else if (Math.abs(f4) <= f3) {
         return f2 + f4;
      } else if (f4 < 0.0F) {
         return f2 - f3;
      } else {
         return f4 > 0.0F ? f2 + f3 : f2;
      }
   }

   public RotationPlanner() {
      this.doubleValue5 = 0.0;
      this.doubleValue = 0.0;
      this.longValue2 = System.currentTimeMillis();
      this.doubleValue9 = 0.0;
      this.doubleValue3 = 0.0;
      this.doubleValue7 = 0.0;
      this.boxList = new ArrayList<>(101);
      this.flag4 = false;
      this.doubleValue4 = 0.8;
      this.flag3 = false;
      this.doubleValue6 = 0.95;
      this.flag = false;
      this.floatValuePair = new Pair(0.35F, 0.5F);
      this.doubleValue2 = 1.0;
      this.longValue = 100L;
      this.flag2 = false;
      this.intValue = 1;
      this.vec3d = Vec3d.ZERO;
   }

   @NotNull
   @SneakyThrows
   public Pair<Float, Float> resolvePair(@NotNull LivingEntity livingEntity) {
      Box box = livingEntity.getBoundingBox();
      if (this.boxList.size() >= 101) {
         this.boxList.removeLast();
      }

      while (this.boxList.size() < 101) {
         this.boxList.addFirst(box);
      }

      assert mc.player != null;
      double d = livingEntity.getY() - mc.player.getY();
      MutableVec3d mutableVec3d = new MutableVec3d();
      Box box2 = this.flag2 ? this.boxList.get(this.intValue) : box;
      if (this.flag4) {
         mutableVec3d.handleVec3d(RotationUtils.createVec3d5(box2, mc.player.getEyePos()));
         if (PlayerUtils.check2() || PlayerUtils.testLivingEntity(livingEntity)) {
            mutableVec3d.handleDouble2(
               MathUtils.computeDouble9(this.doubleValue4 - 1.0, 1.0 - this.doubleValue4) * 0.4,
               MathUtils.computeDouble9(this.doubleValue4 - 1.0, 1.0 - this.doubleValue4) * 0.4,
               MathUtils.computeDouble9(this.doubleValue4 - 1.0, 1.0 - this.doubleValue4) * 0.4
            );
         }
      } else {
         mutableVec3d.handleVec3d(
            new Vec3d((box2.maxX + box2.minX) / 2.0, box2.minY + livingEntity.getStandingEyeHeight() - 0.15, (box2.maxZ + box2.minZ) / 2.0)
         );
      }

      if (d >= 0.0 && this.flag3) {
         if (mutableVec3d.getDoubleValue() - d > livingEntity.getY()) {
            mutableVec3d.handleVec3d(new Vec3d(mutableVec3d.getDoubleValue(), mutableVec3d.getDoubleValue() - d, mutableVec3d.getDoubleValue2()));
         } else {
            mutableVec3d.handleVec3d(new Vec3d(livingEntity.getX(), livingEntity.getY() + 0.2, livingEntity.getZ()));
         }

         if (!livingEntity.isOnGround() && (PlayerUtils.check2() || PlayerUtils.testLivingEntity(livingEntity))) {
            mutableVec3d.handleDouble2(0.0, MathUtils.computeDouble9(this.doubleValue6 - 1.0, 1.0 - this.doubleValue6) * 0.4, 0.0);
         }
      }

      if (this.flag) {
         if (System.currentTimeMillis() - this.longValue2 >= this.longValue) {
            this.doubleValue8 = computeDouble2(((Float)this.floatValuePair.getLeft()).floatValue());
            this.doubleValue5 = computeDouble2(((Float)this.floatValuePair.getRight()).floatValue());
            this.doubleValue = computeDouble2(((Float)this.floatValuePair.getLeft()).floatValue());
            this.longValue2 = System.currentTimeMillis();
         }

         this.doubleValue9 = computeDouble(this.doubleValue8, this.doubleValue9, this.doubleValue2);
         this.doubleValue3 = computeDouble(this.doubleValue5, this.doubleValue3, this.doubleValue2);
         this.doubleValue7 = computeDouble(this.doubleValue, this.doubleValue7, this.doubleValue2);
         mutableVec3d.setDoubleValue3(computeDouble3(box.maxX, box.minX, mutableVec3d.getDoubleValue3() + this.doubleValue9));
         mutableVec3d.setDoubleValue(computeDouble3(box.maxY, box.minY, mutableVec3d.getDoubleValue() + this.doubleValue3));
         mutableVec3d.setDoubleValue2(computeDouble3(box.maxZ, box.minZ, mutableVec3d.getDoubleValue2() + this.doubleValue7));
      }

      Vec3d vec3d = mutableVec3d.createVec3d();
      float[] floats = RotationUtils.values2(mc.player, vec3d);
      float f = floats[0];
      float f2 = floats[1];
      this.vec3d = vec3d;
      return new Pair(f, f2);
   }

   public void handleFlag(boolean flag, @Range(from = 0L, to = 1L) double d) {
      this.doubleValue6 = d;
      this.flag3 = flag;
   }

   public void run2() {
      this.doubleValue8 = 0.0;
      this.doubleValue5 = 0.0;
      this.doubleValue = 0.0;
      this.longValue2 = System.currentTimeMillis();
      this.doubleValue9 = 0.0;
      this.doubleValue3 = 0.0;
      this.doubleValue7 = 0.0;
      this.boxList.clear();
   }

   public static boolean testFloat(float[] floats, float[] floats2) {
      return testFloat2(floats[0], floats[1], floats2[0], floats2[1]);
   }

   public static float computeDouble2(double d) {
      return (float)((Math.random() - 0.5) * 2.0 * d);
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public void handleFlag2(boolean flag, @Range(from = 0L, to = 1L) double d) {
      this.doubleValue4 = d;
      this.flag4 = flag;
   }

   public static double computeDouble3(double d, double d2, double d3) {
      return d3 >= d ? d : Math.max(d3, d2);
   }

   public void handleFlag3(boolean flag, int i) {
      this.intValue = i;
      this.flag2 = flag;
   }

   public static boolean testFloat2(float f, float f2, float f3, float f4) {
      return testFloat3(f, f3) && Math.abs(f2 - f4) < 0.05;
   }

   @NotNull
   @SneakyThrows
   public Pair<Float, Float> resolvePair2(float[] floats) {
      assert mc.player != null;
      double[] doubles = new double[]{floats[0], floats[1]};
      if (this.flag4) {
         doubles[0] += MathUtils.computeDouble9(this.doubleValue4 - 1.0, 1.0 - this.doubleValue4) * 0.4;
         doubles[1] += MathUtils.computeDouble9(this.doubleValue4 - 1.0, 1.0 - this.doubleValue4) * 0.4;
      }

      if (this.flag3) {
         doubles[1] += MathUtils.computeDouble9(this.doubleValue6 - 1.0, 1.0 - this.doubleValue6) * 0.4;
      }

      if (this.flag) {
         if (System.currentTimeMillis() - this.longValue2 >= this.longValue) {
            this.doubleValue8 = computeDouble2(((Float)this.floatValuePair.getLeft()).floatValue());
            this.doubleValue5 = computeDouble2(((Float)this.floatValuePair.getRight()).floatValue());
            this.longValue2 = System.currentTimeMillis();
         }

         this.doubleValue9 = computeDouble(this.doubleValue8, this.doubleValue9, this.doubleValue2);
         this.doubleValue3 = computeDouble(this.doubleValue5, this.doubleValue3, this.doubleValue2);
         doubles[0] += this.doubleValue9;
         doubles[1] += this.doubleValue3;
      }

      float f = (float)doubles[0];
      float f2 = (float)doubles[1];
      return new Pair(f, f2);
   }

   public void handleFlag4(boolean flag, Pair<Float, Float> pair, double d, long l) {
      this.floatValuePair = pair;
      this.doubleValue2 = d / 100.0;
      this.longValue = l;
      this.flag = flag;
   }

   public static boolean testFloat3(float f, float f2) {
      return Math.abs(MathHelper.wrapDegrees(f) - MathHelper.wrapDegrees(f2)) < 0.1;
   }

   public static boolean testVec2f(@NotNull Vec2f vec2f, @NotNull Vec2f vec2f2) {
      return testFloat3(vec2f.x, vec2f2.x) && Math.abs(vec2f.y - vec2f2.y) < 0.1;
   }
}
