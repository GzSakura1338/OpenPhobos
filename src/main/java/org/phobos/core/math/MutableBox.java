package org.phobos.core.math;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MutableBox {
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;
   public double doubleValue4;
   public double doubleValue5;
   public double doubleValue6;

   public void handleDouble(double d, double d2, double d3, double d4, double d5, double d6) {
      this.setDoubleValue6(Math.min(d, d4));
      this.setDoubleValue(Math.min(d2, d5));
      this.setDoubleValue3(Math.min(d3, d6));
      this.setDoubleValue2(Math.max(d, d4));
      this.setDoubleValue5(Math.max(d2, d5));
      this.setDoubleValue4(Math.max(d3, d6));
   }

   public void handleDouble2(double d, double d2, double d3) {
      this.doubleValue6 += d;
      this.doubleValue += d2;
      this.doubleValue3 += d3;
      this.doubleValue2 += d;
      this.doubleValue5 += d2;
      this.doubleValue4 += d3;
   }

   public void handleDouble3(double d, double d2, double d3) {
      this.doubleValue6 -= d;
      this.doubleValue -= d2;
      this.doubleValue3 -= d3;
      this.doubleValue2 += d;
      this.doubleValue5 += d2;
      this.doubleValue4 += d3;
   }

   public void handleVec3d(Vec3d vec3d) {
      this.handleDouble2(vec3d.x, vec3d.y, vec3d.z);
   }

   public void handleInt(int i, int i2, int i3) {
      this.handleDouble(i, i2, i3, i + 1, i2 + 1, i3 + 1);
   }

   @Nullable
   public BlockHitResult createBlockHitResult(Vec3d vec3d, Vec3d vec3d2, BlockPos blockPos) {
      double[] doubles = new double[]{1.0};
      double d = vec3d2.x - vec3d.x;
      double d2 = vec3d2.y - vec3d.y;
      double d3 = vec3d2.z - vec3d.z;
      this.handleBlockPos2(blockPos);
      Direction direction = resolveDirection2(this, vec3d, doubles, null, d, d2, d3);
      if (direction == null) {
         return null;
      }

      double d4 = doubles[0];
      return new BlockHitResult(vec3d.add(d4 * d, d4 * d2, d4 * d3), direction, blockPos, false);
   }

   public void handleDouble4(double d, double d2, double d3) {
      this.doubleValue6 += d;
      this.doubleValue += d2;
      this.doubleValue3 += d3;
   }

   public void handleDouble5(double d) {
      this.handleDouble3(d, d, d);
   }

   public void handleBlockPos(BlockPos blockPos) {
      this.handleDouble(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);
   }

   public double computeVec3d(Vec3d vec3d) {
      double d = Math.max(Math.max(this.doubleValue6 - vec3d.x, vec3d.x - this.doubleValue2), 0.0);
      double d2 = Math.max(Math.max(this.doubleValue - vec3d.y, vec3d.y - this.doubleValue5), 0.0);
      double d3 = Math.max(Math.max(this.doubleValue3 - vec3d.z, vec3d.z - this.doubleValue4), 0.0);
      return MathHelper.squaredMagnitude(d, d2, d3);
   }

   public void handleBox(Box box) {
      this.handleDouble(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
   }

   public void handleDouble6(double d, double d2, double d3) {
      this.doubleValue2 += d;
      this.doubleValue5 += d2;
      this.doubleValue4 += d3;
   }

   public Box createBox() {
      return new Box(
         this.getDoubleValue6(), this.getDoubleValue(), this.getDoubleValue3(), this.getDoubleValue2(), this.getDoubleValue5(), this.getDoubleValue4()
      );
   }

   @Nullable
   public static Direction resolveDirection(
      double[] doubles,
      @Nullable Direction direction,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      Direction direction2,
      double d9,
      double d10,
      double d11
   ) {
      double d12 = (d4 - d9) / d;
      double d13 = d10 + d12 * d2;
      double d14 = d11 + d12 * d3;
      if (0.0 < d12 && d12 < doubles[0] && d5 - 1.0E-7 < d13 && d13 < d6 + 1.0E-7 && d7 - 1.0E-7 < d14 && d14 < d8 + 1.0E-7) {
         doubles[0] = d12;
         return direction2;
      } else {
         return direction;
      }
   }

   @Nullable
   public static Direction resolveDirection2(
      MutableBox mutableBox, Vec3d vec3d, double[] doubles, @Nullable Direction direction, double d, double d2, double d3
   ) {
      if (d > 1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d,
            d2,
            d3,
            mutableBox.doubleValue6,
            mutableBox.doubleValue,
            mutableBox.doubleValue5,
            mutableBox.doubleValue3,
            mutableBox.doubleValue4,
            Direction.WEST,
            vec3d.x,
            vec3d.y,
            vec3d.z
         );
      } else if (d < -1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d,
            d2,
            d3,
            mutableBox.doubleValue2,
            mutableBox.doubleValue,
            mutableBox.doubleValue5,
            mutableBox.doubleValue3,
            mutableBox.doubleValue4,
            Direction.EAST,
            vec3d.x,
            vec3d.y,
            vec3d.z
         );
      }

      if (d2 > 1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d2,
            d3,
            d,
            mutableBox.doubleValue,
            mutableBox.doubleValue3,
            mutableBox.doubleValue4,
            mutableBox.doubleValue6,
            mutableBox.doubleValue2,
            Direction.DOWN,
            vec3d.y,
            vec3d.z,
            vec3d.x
         );
      } else if (d2 < -1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d2,
            d3,
            d,
            mutableBox.doubleValue5,
            mutableBox.doubleValue3,
            mutableBox.doubleValue4,
            mutableBox.doubleValue6,
            mutableBox.doubleValue2,
            Direction.UP,
            vec3d.y,
            vec3d.z,
            vec3d.x
         );
      }

      if (d3 > 1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d3,
            d,
            d2,
            mutableBox.doubleValue3,
            mutableBox.doubleValue6,
            mutableBox.doubleValue2,
            mutableBox.doubleValue,
            mutableBox.doubleValue5,
            Direction.NORTH,
            vec3d.z,
            vec3d.x,
            vec3d.y
         );
      } else if (d3 < -1.0E-7) {
         direction = resolveDirection(
            doubles,
            direction,
            d3,
            d,
            d2,
            mutableBox.doubleValue4,
            mutableBox.doubleValue6,
            mutableBox.doubleValue2,
            mutableBox.doubleValue,
            mutableBox.doubleValue5,
            Direction.SOUTH,
            vec3d.z,
            vec3d.x,
            vec3d.y
         );
      }

      return direction;
   }

   public void handleMutableBox(MutableBox mutableBox) {
      this.handleDouble(
         mutableBox.getDoubleValue6(),
         mutableBox.getDoubleValue(),
         mutableBox.getDoubleValue3(),
         mutableBox.getDoubleValue2(),
         mutableBox.getDoubleValue5(),
         mutableBox.getDoubleValue4()
      );
   }

   public void handleBlockPos2(BlockPos blockPos) {
      this.handleDouble(
         this.doubleValue6 + blockPos.getX(),
         this.doubleValue + blockPos.getY(),
         this.doubleValue3 + blockPos.getZ(),
         this.doubleValue2 + blockPos.getX(),
         this.doubleValue5 + blockPos.getY(),
         this.doubleValue4 + blockPos.getZ()
      );
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public double getDoubleValue5() {
      return this.doubleValue5;
   }

   public void setDoubleValue6(double d) {
      this.doubleValue6 = d;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public void setDoubleValue4(double d) {
      this.doubleValue4 = d;
   }

   @Override
   public String toString() {
      return "MutBox(minX="
         + this.getDoubleValue6()
         + ", minY="
         + this.getDoubleValue()
         + ", minZ="
         + this.getDoubleValue3()
         + ", maxX="
         + this.getDoubleValue2()
         + ", maxY="
         + this.getDoubleValue5()
         + ", maxZ="
         + this.getDoubleValue4()
         + ")";
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public boolean testObject(Object object) {
      return object instanceof MutableBox;
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      long l = Double.doubleToLongBits(this.getDoubleValue6());
      i2 = i2 * 59 + (int)(l >>> 32 ^ l);
      long l2 = Double.doubleToLongBits(this.getDoubleValue());
      i2 = i2 * 59 + (int)(l2 >>> 32 ^ l2);
      long l3 = Double.doubleToLongBits(this.getDoubleValue3());
      i2 = i2 * 59 + (int)(l3 >>> 32 ^ l3);
      long l4 = Double.doubleToLongBits(this.getDoubleValue2());
      i2 = i2 * 59 + (int)(l4 >>> 32 ^ l4);
      long l5 = Double.doubleToLongBits(this.getDoubleValue5());
      i2 = i2 * 59 + (int)(l5 >>> 32 ^ l5);
      long l6 = Double.doubleToLongBits(this.getDoubleValue4());
      return i2 * 59 + (int)(l6 >>> 32 ^ l6);
   }

   public void setDoubleValue5(double d) {
      this.doubleValue5 = d;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public double getDoubleValue6() {
      return this.doubleValue6;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof MutableBox mutableBox)) {
         return false;
      } else if (!mutableBox.testObject(this)) {
         return false;
      } else if (Double.compare(this.getDoubleValue6(), mutableBox.getDoubleValue6()) != 0) {
         return false;
      } else if (Double.compare(this.getDoubleValue(), mutableBox.getDoubleValue()) != 0) {
         return false;
      } else if (Double.compare(this.getDoubleValue3(), mutableBox.getDoubleValue3()) != 0) {
         return false;
      } else if (Double.compare(this.getDoubleValue2(), mutableBox.getDoubleValue2()) != 0) {
         return false;
      } else {
         return Double.compare(this.getDoubleValue5(), mutableBox.getDoubleValue5()) != 0
            ? false
            : Double.compare(this.getDoubleValue4(), mutableBox.getDoubleValue4()) == 0;
      }
   }

   public double getDoubleValue4() {
      return this.doubleValue4;
   }
}
