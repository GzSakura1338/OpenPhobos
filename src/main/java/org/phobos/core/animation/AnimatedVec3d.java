package org.phobos.core.animation;

import net.minecraft.util.math.Vec3d;
import org.phobos.core.math.MathUtils;

public class AnimatedVec3d {
   public boolean flag;
   public final FloatAnimator floatAnimator;
   public final AnimatorGroup animatorGroup;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();
   public final FloatAnimator floatAnimator3 = new FloatAnimator();

   public void handleVec3d(Vec3d vec3d) {
      this.handleDouble2(vec3d.x, vec3d.y, vec3d.z);
   }

   public AnimatedVec3d(double d, double d2, double d3, boolean flag) {
      this.floatAnimator = new FloatAnimator();
      this.animatorGroup = new AnimatorGroup(this.floatAnimator2, this.floatAnimator3, this.floatAnimator);
      this.flag = false;
      this.floatAnimator2.setFloatValue2((float)d);
      this.floatAnimator3.setFloatValue2((float)d2);
      this.floatAnimator.setFloatValue2((float)d3);
      this.flag = flag;
   }

   public void handleVec3d2(Vec3d vec3d) {
      this.handleDouble(vec3d.x, vec3d.y, vec3d.z);
      this.handleDouble2(vec3d.x, vec3d.y, vec3d.z);
   }

   public void run() {
      this.animatorGroup.run();
   }

   public AnimatedVec3d(Vec3d vec3d) {
      this(vec3d.x, vec3d.y, vec3d.z);
   }

   public AnimatedVec3d(double d, double d2, double d3) {
      this(d, d2, d3, true);
   }

   public void handleVec3d3(Vec3d vec3d) {
      this.handleVec3d2(vec3d);
      this.flag = true;
   }

   public Vec3d createVec3d() {
      return new Vec3d(
         MathUtils.computeFloat5(this.floatAnimator2.getFloatValue2(), 3),
         MathUtils.computeFloat5(this.floatAnimator3.getFloatValue2(), 3),
         MathUtils.computeFloat5(this.floatAnimator.getFloatValue2(), 3)
      );
   }

   public AnimatedVec3d() {
      this(0.0, 0.0, 0.0, false);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleDouble(double d, double d2, double d3) {
      this.floatAnimator2.setFloatValue2((float)d);
      this.floatAnimator3.setFloatValue2((float)d2);
      this.floatAnimator.setFloatValue2((float)d3);
   }

   public void handleFloat(float f, float f2) {
      this.animatorGroup.handleFloat(f, f2);
   }

   public void handleDouble2(double d, double d2, double d3) {
      this.floatAnimator2.setFloatValue((float)d);
      this.floatAnimator3.setFloatValue((float)d2);
      this.floatAnimator.setFloatValue((float)d3);
   }
}
