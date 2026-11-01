package org.phobos.core.math;

import net.minecraft.util.math.Vec3d;

public class MutableVec3d {
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public void handleDouble(double d, double d2, double d3) {
      this.setDoubleValue3(d);
      this.setDoubleValue(d2);
      this.setDoubleValue2(d3);
   }

   public Vec3d createVec3d() {
      return new Vec3d(this.getDoubleValue3(), this.getDoubleValue(), this.getDoubleValue2());
   }

   public void handleDouble2(double d, double d2, double d3) {
      this.handleDouble(this.getDoubleValue3() + d, this.getDoubleValue() + d2, this.getDoubleValue2() + d3);
   }

   @Override
   public String toString() {
      return "MutVec3d(x=" + this.getDoubleValue3() + ", y=" + this.getDoubleValue() + ", z=" + this.getDoubleValue2() + ")";
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof MutableVec3d mutableVec3d)) {
         return false;
      } else if (!mutableVec3d.testObject(this)) {
         return false;
      } else if (Double.compare(this.getDoubleValue3(), mutableVec3d.getDoubleValue3()) != 0) {
         return false;
      } else {
         return Double.compare(this.getDoubleValue(), mutableVec3d.getDoubleValue()) != 0
            ? false
            : Double.compare(this.getDoubleValue2(), mutableVec3d.getDoubleValue2()) == 0;
      }
   }

   public void handleVec3d(Vec3d vec3d) {
      this.handleDouble(vec3d.x, vec3d.y, vec3d.z);
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public double computeDouble(double d, double d2, double d3) {
      double d4 = d - this.getDoubleValue3();
      double d5 = d2 - this.getDoubleValue();
      double d6 = d3 - this.getDoubleValue2();
      return d4 * d4 + d5 * d5 + d6 * d6;
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      long l = Double.doubleToLongBits(this.getDoubleValue3());
      i2 = i2 * 59 + (int)(l >>> 32 ^ l);
      long l2 = Double.doubleToLongBits(this.getDoubleValue());
      i2 = i2 * 59 + (int)(l2 >>> 32 ^ l2);
      long l3 = Double.doubleToLongBits(this.getDoubleValue2());
      return i2 * 59 + (int)(l3 >>> 32 ^ l3);
   }

   public void run() {
      double d = Math.sqrt(
         this.getDoubleValue3() * this.getDoubleValue3() + this.getDoubleValue() * this.getDoubleValue() + this.getDoubleValue2() * this.getDoubleValue2()
      );
      if (d < 1.0E-4) {
         this.handleDouble(0.0, 0.0, 0.0);
      } else {
         this.handleDouble(this.getDoubleValue3() / d, this.getDoubleValue() / d, this.getDoubleValue2() / d);
      }
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public boolean testObject(Object object) {
      return object instanceof MutableVec3d;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public void handleDouble3(double d) {
      this.handleDouble(this.getDoubleValue3() * d, this.getDoubleValue() * d, this.getDoubleValue2() * d);
   }

   public void handleMutableVec3d(MutableVec3d mutableVec3d) {
      this.handleDouble(mutableVec3d.getDoubleValue3(), mutableVec3d.getDoubleValue(), mutableVec3d.getDoubleValue2());
   }
}
