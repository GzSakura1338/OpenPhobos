package org.phobos.event;

import net.minecraft.util.math.Vec3d;

public class UpdateVelocityEvent {
   public final float floatValue;
   public Vec3d vec3d;
   public final Vec3d vec3d2;
   public final float floatValue2;

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public Vec3d getVec3d2() {
      return this.vec3d2;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public UpdateVelocityEvent(Vec3d vec3d, float f, float f2, Vec3d vec3d2) {
      this.vec3d2 = vec3d;
      this.floatValue = f;
      this.floatValue2 = f2;
      this.vec3d = vec3d2;
   }
}
