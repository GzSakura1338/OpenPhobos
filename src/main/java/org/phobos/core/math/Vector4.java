package org.phobos.core.math;

public class Vector4 {
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;

   public Vector4() {
   }

   @Override
   public int hashCode() {
      int i = 59 + Float.floatToIntBits(this.floatValue4);
      return ((i * 59 + Float.floatToIntBits(this.floatValue3)) * 59 + Float.floatToIntBits(this.floatValue2)) * 59 + Float.floatToIntBits(this.floatValue);
   }

   public Vector4(float f, float f2, float f3, float f4) {
      this.floatValue4 = f;
      this.floatValue3 = f2;
      this.floatValue2 = f3;
      this.floatValue = f4;
   }

   @Override
   public String toString() {
      return "Vector4(x=" + this.floatValue4 + ", y=" + this.floatValue3 + ", z=" + this.floatValue2 + ", w=" + this.floatValue + ")";
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else {
         return !(object instanceof Vector4 vector4)
            ? false
            : vector4.testObject(this)
               && Float.compare(this.floatValue4, vector4.floatValue4) == 0
               && Float.compare(this.floatValue3, vector4.floatValue3) == 0
               && Float.compare(this.floatValue2, vector4.floatValue2) == 0
               && Float.compare(this.floatValue, vector4.floatValue) == 0;
      }
   }

   public Vector3 createVector3() {
      return new Vector3(this.floatValue4, this.floatValue3, this.floatValue2);
   }

   public boolean testObject(Object object) {
      return object instanceof Vector4;
   }

   public Vector4 clone() {
      return new Vector4(this.floatValue4, this.floatValue3, this.floatValue2, this.floatValue);
   }
}
