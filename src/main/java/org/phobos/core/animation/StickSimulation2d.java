package org.phobos.core.animation;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.Animatable;
import org.phobos.api.VectorFunction;
import org.phobos.core.math.Vector3;

public class StickSimulation2d implements Animatable {
   public int intValue;
   public float floatValue;
   public boolean flag;
   public List<StickSimulation2d.Stick> stickList;
   public float floatValue2;
   public StickSimulation2d.Vector2 vector2;
   public List<StickSimulation2d.Point> pointList = new ArrayList<>();

   @Override
   public void setVector3(Vector3 vector3) {
      this.vector2.floatValue = vector3.floatValue3;
      this.vector2.floatValue2 = vector3.floatValue;
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }

   public double computeVector2(StickSimulation2d.Vector2 vector2, StickSimulation2d.Vector2 vector22, StickSimulation2d.Vector2 vector23) {
      return Math.atan2(vector23.floatValue2 - vector2.floatValue2, vector23.floatValue - vector2.floatValue)
         - Math.atan2(vector22.floatValue2 - vector2.floatValue2, vector22.floatValue - vector2.floatValue);
   }

   public StickSimulation2d.Vector2 createVector2(StickSimulation2d.Vector2 vector2, StickSimulation2d.Vector2 vector22, double d, double d2) {
      double d3 = d2 / 57.2958;
      float f = vector22.floatValue - vector2.floatValue;
      float f2 = vector22.floatValue2 - vector2.floatValue2;
      if (d < 0.0) {
         d3 *= -1.0;
      }

      double d4 = Math.cos(d3);
      double d5 = Math.sin(d3);
      return new StickSimulation2d.Vector2((float)(f * d4 - f2 * d5 + vector2.floatValue), (float)(f * d5 + f2 * d4 + vector2.floatValue2));
   }

   @Override
   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   @Override
   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public boolean check() {
      return this.stickList.isEmpty();
   }

   @Override
   public float getFloatValue2() {
      return this.floatValue2;
   }

   @Override
   public void run3() {
      StickSimulation2d.Vector2 vector24 = this.vector2.clone().resolveVector24(this.floatValue2 * 0.05F);
      StickSimulation2d.Vector2 vector25 = new StickSimulation2d.Vector2(0.0F, 0.0F);

      for (StickSimulation2d.Point point : this.pointList) {
         if (!point.flag) {
            vector25.handleVector2(point.vector2);
            point.vector2.resolveVector22(vector24);
            point.vector22.handleVector2(vector25);
         }
      }

      StickSimulation2d.Point point3 = this.pointList.get(0);

      for (StickSimulation2d.Point point2 : this.pointList) {
         if (point2 != point3 && point2.vector2.floatValue - point3.vector2.floatValue > 0.0F) {
            point2.vector2.floatValue = point3.vector2.floatValue;
         }
      }

      for (int i = this.pointList.size() - 2; i >= 1; i--) {
         double d = this.computeVector2(this.pointList.get(i).vector2, this.pointList.get(i - 1).vector2, this.pointList.get(i + 1).vector2) * 57.2958;
         if (d > 360.0) {
            d -= 360.0;
         }

         if (d < -360.0) {
            d += 360.0;
         }

         double d2 = Math.abs(d);
         if (d2 < 180.0F - this.floatValue) {
            StickSimulation2d.Point var10000 = this.pointList.get(i + 1);
            float var10005 = 180.0F - this.floatValue;
            var10000.vector2 = this.createVector2(this.pointList.get(i).vector2, this.pointList.get(i - 1).vector2, d, var10005 + 1.0F);
         }

         if (d2 > 180.0F + this.floatValue) {
            StickSimulation2d.Point var19 = this.pointList.get(i + 1);
            float var20 = 180.0F + this.floatValue;
            var19.vector2 = this.createVector2(this.pointList.get(i).vector2, this.pointList.get(i - 1).vector2, d, var20 - 1.0F);
         }
      }

      for (int i3 = 0; i3 < this.intValue; i3++) {
         for (int i2 = this.stickList.size() - 1; i2 >= 0; i2--) {
            StickSimulation2d.Stick stick = this.stickList.get(i2);
            StickSimulation2d.Vector2 vector2 = stick.point.vector2.clone().resolveVector22(stick.point2.vector2).resolveVector2(2.0F);
            StickSimulation2d.Vector2 vector22 = stick.point.vector2.clone().resolveVector23(stick.point2.vector2).getVector2();
            if (!stick.point.flag) {
               stick.point.vector2 = vector2.clone().resolveVector22(vector22.clone().resolveVector24(stick.floatValue / 2.0F));
            }

            if (!stick.point2.flag) {
               stick.point2.vector2 = vector2.clone().resolveVector23(vector22.clone().resolveVector24(stick.floatValue / 2.0F));
            }
         }
      }

      for (StickSimulation2d.Stick stick2 : this.stickList) {
         StickSimulation2d.Vector2 vector23 = stick2.point.vector2.clone().resolveVector23(stick2.point2.vector2).getVector2();
         if (!stick2.point2.flag) {
            stick2.point2.vector2 = stick2.point.vector2.clone().resolveVector23(vector23.resolveVector24(stick2.floatValue));
         }
      }
   }

   @Override
   public List<VectorFunction> getList() {
      return (List<VectorFunction>)(List<?>)this.pointList;
   }

   @Override
   public boolean testInt(int i) {
      if (this.pointList.size() != i) {
         this.pointList.clear();
         this.stickList.clear();

         for (int i2 = 0; i2 < i; i2++) {
            StickSimulation2d.Point point = new StickSimulation2d.Point();
            point.vector2.floatValue2 = -i2;
            point.flag = i2 == 0;
            this.pointList.add(point);
            if (i2 > 0) {
               this.stickList.add(new StickSimulation2d.Stick(this.pointList.get(i2 - 1), point, 1.0F));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void handleVector3(Vector3 vector3) {
      this.pointList.getFirst().vector22.handleVector2(this.pointList.getFirst().vector2);
      this.pointList.getFirst().vector2.resolveVector22(new StickSimulation2d.Vector2(vector3.floatValue3, vector3.floatValue));
   }

   public StickSimulation2d() {
      this.stickList = new ArrayList<>();
      this.vector2 = new StickSimulation2d.Vector2(0.0F, -1.0F);
      this.floatValue2 = 0.0F;
      this.intValue = 30;
      this.floatValue = 5.0F;
      this.flag = false;
   }

   public static class Point implements VectorFunction {
      public StickSimulation2d.Vector2 vector2 = new StickSimulation2d.Vector2(0.0F, 0.0F);
      public StickSimulation2d.Vector2 vector22 = new StickSimulation2d.Vector2(0.0F, 0.0F);
      public boolean flag;

      @Override
      public float computeFloat(float f) {
         return MathHelper.lerp(f, this.vector22.floatValue, this.vector2.floatValue);
      }

      @Override
      public float computeFloat2(float f) {
         return 0.0F;
      }

      @Override
      public float computeFloat3(float f) {
         return MathHelper.lerp(f, this.vector22.floatValue2, this.vector2.floatValue2);
      }
   }

   public static class Stick {
      public float floatValue;
      public StickSimulation2d.Point point;
      public StickSimulation2d.Point point2;

      public Stick(StickSimulation2d.Point point, StickSimulation2d.Point point2, float f) {
         this.point = point;
         this.point2 = point2;
         this.floatValue = f;
      }
   }

   public static class Vector2 {
      public float floatValue;
      public float floatValue2;

      public StickSimulation2d.Vector2 resolveVector2(float f) {
         this.floatValue /= f;
         this.floatValue2 /= f;
         return this;
      }

      @Override
      public boolean equals(Object object) {
         if (object == this) {
            return true;
         } else {
            return !(object instanceof StickSimulation2d.Vector2 vector2)
               ? false
               : vector2.testObject(this)
                  && Float.compare(this.floatValue, vector2.floatValue) == 0
                  && Float.compare(this.floatValue2, vector2.floatValue2) == 0;
         }
      }

      public StickSimulation2d.Vector2 resolveVector22(StickSimulation2d.Vector2 vector2) {
         this.floatValue = this.floatValue + vector2.floatValue;
         this.floatValue2 = this.floatValue2 + vector2.floatValue2;
         return this;
      }

      @Override
      public int hashCode() {
         int i = 59 + Float.floatToIntBits(this.floatValue);
         return i * 59 + Float.floatToIntBits(this.floatValue2);
      }

      @Override
      public String toString() {
         return "StickSimulation.Vector2(x=" + this.floatValue + ", y=" + this.floatValue2 + ")";
      }

      public boolean testObject(Object object) {
         return object instanceof StickSimulation2d.Vector2;
      }

      public StickSimulation2d.Vector2 resolveVector23(StickSimulation2d.Vector2 vector2) {
         this.floatValue = this.floatValue - vector2.floatValue;
         this.floatValue2 = this.floatValue2 - vector2.floatValue2;
         return this;
      }

      public StickSimulation2d.Vector2 resolveVector24(float f) {
         this.floatValue *= f;
         this.floatValue2 *= f;
         return this;
      }

      public void handleVector2(StickSimulation2d.Vector2 vector2) {
         this.floatValue = vector2.floatValue;
         this.floatValue2 = vector2.floatValue2;
      }

      public StickSimulation2d.Vector2 resolveVector25(float f) {
         float f2 = this.floatValue;
         float f3 = this.floatValue2;
         float f4 = (float)Math.toRadians(f);
         this.floatValue = MathHelper.cos(f4) * f2 - MathHelper.sin(f4) * f3;
         this.floatValue2 = MathHelper.sin(f4) * f2 + MathHelper.cos(f4) * f3;
         return this;
      }

      public StickSimulation2d.Vector2 getVector2() {
         float f = MathHelper.sqrt(this.floatValue * this.floatValue + this.floatValue2 * this.floatValue2);
         if (f < 1.0E-4F) {
            this.floatValue = 0.0F;
            this.floatValue2 = 0.0F;
         } else {
            this.floatValue /= f;
            this.floatValue2 /= f;
         }

         return this;
      }

      public Vector2(float f, float f2) {
         this.floatValue = f;
         this.floatValue2 = f2;
      }

      public StickSimulation2d.Vector2 clone() {
         return new StickSimulation2d.Vector2(this.floatValue, this.floatValue2);
      }
   }
}
