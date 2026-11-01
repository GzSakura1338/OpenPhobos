package org.phobos.event;

import net.minecraft.util.math.Vec3d;

public class MoveEvent {
   public Vec3d vec3d;

   public void handleDouble(double d) {
      this.vec3d = new Vec3d(d, this.vec3d.getY(), this.vec3d.getZ());
   }

   public void handleDouble2(double d) {
      this.vec3d = new Vec3d(this.vec3d.getX(), this.vec3d.getY(), d);
   }

   public MoveEvent(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public void handleDouble3(double d) {
      this.vec3d = new Vec3d(this.vec3d.getX(), d, this.vec3d.getZ());
   }
}
