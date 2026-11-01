package org.phobos.core.math;

import net.minecraft.util.math.MathHelper;

public class Vector3 {
   public float floatValue;
   public float floatValue2;
   public float floatValue3;

   public Vector3() {
   }

   public Vector3 resolveVector3(float f) {
      this.floatValue3 /= f;
      this.floatValue /= f;
      this.floatValue2 /= f;
      return this;
   }

   public Vector3 resolveVector32(Vector3 vector3) {
      float f = this.floatValue3;
      float f2 = this.floatValue;
      float f3 = this.floatValue2;
      float f4 = vector3.floatValue3;
      float f5 = vector3.floatValue;
      float f6 = vector3.floatValue2;
      this.floatValue3 = f2 * f6 - f3 * f5;
      this.floatValue = f3 * f4 - f * f6;
      this.floatValue2 = f * f5 - f2 * f4;
      return this;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else {
         return !(object instanceof Vector3 vector3)
            ? false
            : vector3.testObject(this)
               && Float.compare(this.floatValue3, vector3.floatValue3) == 0
               && Float.compare(this.floatValue, vector3.floatValue) == 0
               && Float.compare(this.floatValue2, vector3.floatValue2) == 0;
      }
   }

   public Vector3 getVector3() {
      float f = MathHelper.sqrt(this.floatValue3 * this.floatValue3 + this.floatValue * this.floatValue + this.floatValue2 * this.floatValue2);
      if (f < 1.0E-4F) {
         this.floatValue3 = 0.0F;
         this.floatValue = 0.0F;
         this.floatValue2 = 0.0F;
      } else {
         this.floatValue3 /= f;
         this.floatValue /= f;
         this.floatValue2 /= f;
      }

      return this;
   }

   public Vector3 clone() {
      return new Vector3(this.floatValue3, this.floatValue, this.floatValue2);
   }

   public Vector3(float f, float f2, float f3) {
      this.floatValue3 = f;
      this.floatValue = f2;
      this.floatValue2 = f3;
   }

   public Vector3 resolveVector33(float f) {
      float f2 = this.floatValue3;
      float f3 = this.floatValue;
      float f4 = (float)Math.toRadians(f);
      this.floatValue3 = MathHelper.cos(f4) * f2 - MathHelper.sin(f4) * f3;
      this.floatValue = MathHelper.sin(f4) * f2 + MathHelper.cos(f4) * f3;
      return this;
   }

   public Vector3 resolveVector34(Vector3 vector3) {
      this.floatValue3 = this.floatValue3 - vector3.floatValue3;
      this.floatValue = this.floatValue - vector3.floatValue;
      this.floatValue2 = this.floatValue2 - vector3.floatValue2;
      return this;
   }

   public boolean testObject(Object object) {
      return object instanceof Vector3;
   }

   public Vector3 resolveVector35(Vector3 vector3) {
      this.floatValue3 = this.floatValue3 + vector3.floatValue3;
      this.floatValue = this.floatValue + vector3.floatValue;
      this.floatValue2 = this.floatValue2 + vector3.floatValue2;
      return this;
   }

   public Vector3 resolveVector36(float f) {
      this.floatValue3 *= f;
      this.floatValue *= f;
      this.floatValue2 *= f;
      return this;
   }

   public Vector3 resolveVector37(float f, float f2, float f3) {
      this.floatValue3 += f;
      this.floatValue += f2;
      this.floatValue2 += f3;
      return this;
   }

   @Override
   public String toString() {
      return "Vector3(x=" + this.floatValue3 + ", y=" + this.floatValue + ", z=" + this.floatValue2 + ")";
   }

   public void handleVector3(Vector3 vector3) {
      this.floatValue3 = vector3.floatValue3;
      this.floatValue = vector3.floatValue;
      this.floatValue2 = vector3.floatValue2;
   }

   @Override
   public int hashCode() {
      int i = 59 + Float.floatToIntBits(this.floatValue3);
      return (i * 59 + Float.floatToIntBits(this.floatValue)) * 59 + Float.floatToIntBits(this.floatValue2);
   }

   public float value() {
      return this.floatValue3 * this.floatValue3 + this.floatValue * this.floatValue + this.floatValue2 * this.floatValue2;
   }
}
