package org.phobos.core.rotation;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class RotationState {
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;
   public float floatValue5;
   public float floatValue6;

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public float getFloatValue6() {
      return this.floatValue6;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f, float f2) {
      this.floatValue3 = this.floatValue6;
      this.floatValue4 = this.floatValue2;
      this.floatValue = this.floatValue5;
      this.floatValue5 = this.computeClientPlayerEntity(clientPlayerEntity, f, this.floatValue);
      this.floatValue6 = f;
      this.floatValue2 = f2;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, float f, float f2) {
      float f5 = f2;
      double d = clientPlayerEntity.getX() - clientPlayerEntity.prevX;
      double d2 = clientPlayerEntity.getZ() - clientPlayerEntity.prevZ;
      if (d * d + d2 * d2 > 0.0025000002F) {
         float f4 = (float)MathHelper.atan2(d2, d) * (180.0F / (float)Math.PI) - 90.0F;
         float f3 = MathHelper.abs(MathHelper.wrapDegrees(f) - f4);
         if (95.0F < f3 && f3 < 265.0F) {
            f5 = f4 - 180.0F;
         } else {
            f5 = f4;
         }
      }

      if (clientPlayerEntity.handSwingProgress > 0.0F) {
         f5 = f;
      }

      f5 = f2 + MathHelper.wrapDegrees(f5 - f2) * 0.3F;
      float f6 = MathHelper.wrapDegrees(f - f5);
      if (f6 < -75.0F) {
         f6 = -75.0F;
      } else if (f6 >= 75.0F) {
         f6 = 75.0F;
      }

      f5 = f - f6;
      if (f6 * f6 > 2500.0F) {
         f5 += f6 * 0.2F;
      }

      return f5;
   }

   public float getFloatValue5() {
      return this.floatValue5;
   }
}
