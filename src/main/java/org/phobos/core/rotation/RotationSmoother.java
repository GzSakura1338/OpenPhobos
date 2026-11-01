package org.phobos.core.rotation;

import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.core.animation.TimedFloatAnimator;

public class RotationSmoother {
   public final TimedFloatAnimator timedFloatAnimator = new TimedFloatAnimator();
   public final TimedFloatAnimator timedFloatAnimator2 = new TimedFloatAnimator();

   public void handleRotation(RotationManager.Rotation rotation) {
      this.timedFloatAnimator.setFloatValue(rotation.getFloatValue2());
      this.timedFloatAnimator2.setFloatValue(rotation.getFloatValue());
   }

   public void handleFloat(float f, float f2) {
      this.timedFloatAnimator.handleFloat3(f, f2);
   }

   public void handleFloat2(float[] floats) {
      this.handleRotation(RotationManager.Rotation.createRotation3(floats));
   }

   public void run() {
      this.timedFloatAnimator.run6();
      this.timedFloatAnimator2.run6();
   }

   public float[] values() {
      return new float[]{
         this.timedFloatAnimator.getFloatValue() - this.timedFloatAnimator.getFloatValue2(),
         this.timedFloatAnimator2.getFloatValue() - this.timedFloatAnimator2.getFloatValue2()
      };
   }

   public boolean testFloat(float f) {
      return this.timedFloatAnimator.testFloat(f) && this.timedFloatAnimator2.testFloat(f);
   }

   public boolean isFinished() {
      return this.timedFloatAnimator.isFinished() && this.timedFloatAnimator2.isFinished();
   }

   public RotationManager.Rotation createRotation() {
      return new RotationManager.Rotation(this.timedFloatAnimator.getFloatValue2(), this.timedFloatAnimator2.getFloatValue2());
   }

   public void handleFloat3(float f, float f2) {
      this.timedFloatAnimator2.handleFloat3(f, f2);
   }

   public void handleFloat4(float f, float f2) {
      this.handleRotation(new RotationManager.Rotation(f, f2));
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, boolean[] flags) {
      if (flags[0]) {
         clientPlayerEntity.setYaw(this.timedFloatAnimator.getFloatValue2());
      }

      if (flags[1]) {
         clientPlayerEntity.setPitch(this.timedFloatAnimator2.getFloatValue2());
      }
   }

   public void handleFloat5(float f, float f2) {
      this.timedFloatAnimator.handleFloat3(f, f2);
      this.timedFloatAnimator2.handleFloat3(f, f2);
   }

   public void run2() {
      this.timedFloatAnimator.setFloatValue2(this.timedFloatAnimator.getFloatValue());
      this.timedFloatAnimator2.setFloatValue2(this.timedFloatAnimator2.getFloatValue());
   }

   public void handleRotation2(RotationManager.Rotation rotation) {
      this.timedFloatAnimator.setFloatValue2(rotation.getFloatValue2());
      this.timedFloatAnimator2.setFloatValue2(rotation.getFloatValue());
      this.handleRotation(rotation);
   }
}
