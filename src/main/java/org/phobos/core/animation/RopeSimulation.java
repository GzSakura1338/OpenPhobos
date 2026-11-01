package org.phobos.core.animation;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.Animatable;
import org.phobos.api.VectorFunction;
import org.phobos.core.math.Vector3;

public class RopeSimulation implements Animatable {
   public float floatValue;
   public List<RopeSimulation.Stick> stickList;
   public int intValue;
   public Vector3 vector3;
   public float floatValue2;
   public List<RopeSimulation.Point> pointList = new ArrayList<>();
   public boolean flag;

   public void run() {
      int i3 = 0;

      do {
         boolean flag = false;

         for (int i2 = 0; i2 < this.pointList.size(); i2++) {
            for (int i = i2 + 1; i < this.pointList.size(); i++) {
               RopeSimulation.Point point = this.pointList.get(i2);
               RopeSimulation.Point point2 = this.pointList.get(i);
               Vector3 vector32 = point.vector3.clone().resolveVector34(point2.vector3);
               if (vector32.value() < 0.99) {
                  flag = true;
                  i3++;
                  vector32.getVector3();
                  Vector3 vector3 = point.vector3.clone().resolveVector35(point2.vector3).resolveVector3(2.0F);
                  if (!point.flag) {
                     point.vector3 = vector3.clone().resolveVector35(vector32.clone().resolveVector36(0.5F));
                  }

                  if (!point2.flag) {
                     point2.vector3 = vector3.clone().resolveVector34(vector32.clone().resolveVector36(0.5F));
                  }
               }
            }
         }

         if (!flag) {
            return;
         }
      } while (i3 < 10);
   }

   public void run7() {
      RopeSimulation.Point point2 = this.pointList.get(0);

      for (int i = 1; i < this.pointList.size(); i++) {
         RopeSimulation.Point point = this.pointList.get(i);
         if (point.vector3.floatValue3 - point2.vector3.floatValue3 > 0.0F) {
            point.vector3.floatValue3 = point2.vector3.floatValue3;
         }

         float f = (float)i / this.pointList.size() * ((float)i / this.pointList.size()) * 5.0F;
         float f2 = point2.vector3.floatValue2 - point.vector3.floatValue2;
         if (f2 > f) {
            point.vector3.floatValue2 = point2.vector3.floatValue2 - f;
         }

         if (f2 < -f) {
            point.vector3.floatValue2 = point2.vector3.floatValue2 + f;
         }
      }
   }

   @Override
   public void handleVector3(Vector3 vector3) {
      this.pointList.getFirst().vector32.handleVector3(this.pointList.getFirst().vector3);
      this.pointList.getFirst().vector3.resolveVector35(vector3);
   }

   @Override
   public float getFloatValue2() {
      return this.floatValue2;
   }

   @Override
   public void setVector3(Vector3 vector3) {
      this.vector3 = vector3;
   }

   public double computeVector3(Vector3 vector3, Vector3 vector32, Vector3 vector33) {
      float f = vector32.floatValue3 - vector3.floatValue3;
      float f2 = vector32.floatValue - vector3.floatValue;
      float f3 = vector32.floatValue3 - vector33.floatValue3;
      float f4 = vector32.floatValue - vector33.floatValue;
      float f5 = f * f3 + f2 * f4;
      float f6 = f * f4 - f2 * f3;
      double d = MathHelper.atan2(f6, f5);
      return d * 180.0 / Math.PI;
   }

   @Override
   public boolean check() {
      return this.stickList.isEmpty();
   }

   @Override
   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   @Override
   public boolean isFlag() {
      return this.flag;
   }

   public RopeSimulation() {
      this.stickList = new ArrayList<>();
      this.vector3 = new Vector3(0.0F, -1.0F, 0.0F);
      this.floatValue2 = 25.0F;
      this.intValue = 30;
      this.floatValue = 20.0F;
      this.flag = false;
   }

   public void run2() {
      for (int i2 = 0; i2 < this.intValue; i2++) {
         for (int i = this.stickList.size() - 1; i >= 0; i--) {
            RopeSimulation.Stick stick = this.stickList.get(i);
            Vector3 vector3 = stick.point.vector3.clone().resolveVector35(stick.point2.vector3).resolveVector3(2.0F);
            Vector3 vector32 = stick.point.vector3.clone().resolveVector34(stick.point2.vector3).getVector3();
            if (!stick.point.flag) {
               stick.point.vector3 = vector3.clone().resolveVector35(vector32.clone().resolveVector36(stick.floatValue / 2.0F));
            }

            if (!stick.point2.flag) {
               stick.point2.vector3 = vector3.clone().resolveVector34(vector32.clone().resolveVector36(stick.floatValue / 2.0F));
            }
         }
      }
   }

   public void run6() {
      Vector3 vector3 = this.vector3.clone().resolveVector36(this.floatValue2 * 0.05F);
      Vector3 vector32 = new Vector3(0.0F, 0.0F, 0.0F);

      for (RopeSimulation.Point point : this.pointList) {
         if (!point.flag) {
            vector32.handleVector3(point.vector3);
            point.vector3.resolveVector35(vector3);
            point.vector32.handleVector3(vector32);
         }
      }
   }

   public void run5() {
      for (RopeSimulation.Stick stick : this.stickList) {
         Vector3 vector3 = stick.point.vector3.clone().resolveVector34(stick.point2.vector3).getVector3();
         if (!stick.point2.flag) {
            stick.point2.vector3 = stick.point.vector3.clone().resolveVector34(vector3.resolveVector36(stick.floatValue));
         }
      }
   }

   public Vector3 resolveVector3(Vector3 vector3, Vector3 vector32, double d) {
      Vector3 vector33 = vector3.clone().resolveVector34(vector32);
      vector33.resolveVector33((float)d).resolveVector35(vector3);
      return vector33;
   }

   @Override
   public boolean testInt(int i) {
      if (this.pointList.size() != i) {
         this.pointList.clear();
         this.stickList.clear();

         for (int i2 = 0; i2 < i; i2++) {
            RopeSimulation.Point point = new RopeSimulation.Point();
            point.vector3.floatValue = -i2;
            point.flag = i2 == 0;
            this.pointList.add(point);
            if (i2 > 0) {
               this.stickList.add(new RopeSimulation.Stick(this.pointList.get(i2 - 1), point, 1.0F));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void run4() {
      for (int i = 1; i < this.pointList.size() - 1; i++) {
         double d = this.computeVector3(this.pointList.get(i).vector3, this.pointList.get(i - 1).vector3, this.pointList.get(i + 1).vector3);
         float f = this.floatValue;
         if (i != this.pointList.size() / 2) {
            f = 0.0F;
         }

         if (d < -f) {
            this.pointList.get(i + 1).vector3 = this.resolveVector3(
               this.pointList.get(i).vector3, this.pointList.get(i - 1).vector3, -f * 2.0F
            );
         }

         if (d > f) {
            this.pointList.get(i + 1).vector3 = this.resolveVector3(
               this.pointList.get(i).vector3, this.pointList.get(i - 1).vector3, f * 2.0F
            );
         }
      }
   }

   @Override
   public List<VectorFunction> getList() {
      return (List<VectorFunction>)(List<?>)this.pointList;
   }

   @Override
   public void run3() {
      this.run6();
      this.run7();
      this.run2();
      this.run4();
      this.run();
      this.run5();
   }

   public static class Point implements VectorFunction {
      public boolean flag;
      public Vector3 vector3 = new Vector3(0.0F, 0.0F, 0.0F);
      public Vector3 vector32 = new Vector3(0.0F, 0.0F, 0.0F);

      @Override
      public float computeFloat2(float f) {
         return MathHelper.lerp(f, this.vector32.floatValue2, this.vector3.floatValue2);
      }

      @Override
      public float computeFloat(float f) {
         return MathHelper.lerp(f, this.vector32.floatValue3, this.vector3.floatValue3);
      }

      @Override
      public float computeFloat3(float f) {
         return MathHelper.lerp(f, this.vector32.floatValue, this.vector3.floatValue);
      }
   }

   public static class Stick {
      public RopeSimulation.Point point;
      public RopeSimulation.Point point2;
      public float floatValue;

      public Stick(RopeSimulation.Point point, RopeSimulation.Point point2, float f) {
         this.point = point;
         this.point2 = point2;
         this.floatValue = f;
      }
   }
}
