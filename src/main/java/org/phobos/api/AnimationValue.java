package org.phobos.api;

public interface AnimationValue {
   interface Double {
      void handleDouble(double var1, double var3);

      double getDoubleValue2();

      void run2();

      void setDoubleValue(double var1);

      void setDoubleValue2(double var1);

      double getDoubleValue();
   }

   interface Factory<S> {
      S getObject();
   }

   interface Float {
      float getFloatValue();

      void run2();

      float getFloatValue2();

      void handleFloat3(float var1, float var2);

      void setFloatValue2(float var1);

      void setFloatValue(float var1);
   }
}
