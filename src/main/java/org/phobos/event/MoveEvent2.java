package org.phobos.event;

import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.CancellableEvent;

public class MoveEvent2 extends CancellableEvent {
   public boolean flag;
   public Vec3d vec3d;

   public boolean isFlag() {
      return this.flag;
   }

   public void setVec3d(Vec3d vec3d) {
      this.vec3d = vec3d;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public void setDoubleValue2(double d) {
      this.setVec3d(new Vec3d(this.vec3d.x, this.vec3d.y, d));
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof MoveEvent2 moveEvent2)) {
         return false;
      } else {
         if (!moveEvent2.testObject(this)) {
            return false;
         }

         if (!super.equals(object)) {
            return false;
         }

         if (this.isFlag() != moveEvent2.isFlag()) {
            return false;
         }

         Object object2 = this.getVec3d();
         Object object3 = moveEvent2.getVec3d();
         return object2 == null ? object3 == null : object2.equals(object3);
      }
   }

   public MoveEvent2(Vec3d vec3d, boolean flag) {
      this.vec3d = vec3d;
      this.flag = flag;
   }

   public double getDoubleValue3() {
      return this.vec3d.y;
   }

   public void setDoubleValue(double d) {
      this.setVec3d(new Vec3d(this.vec3d.x, d, this.vec3d.z));
   }

   public double getDoubleValue() {
      return this.vec3d.z;
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = super.hashCode();
      i2 = i2 * 59 + (this.isFlag() ? 79 : 97);
      Object object = this.getVec3d();
      return i2 * 59 + (object == null ? 43 : object.hashCode());
   }

   public void setDoubleValue3(double d) {
      this.setVec3d(new Vec3d(d, this.vec3d.y, this.vec3d.z));
   }

   @Override
   public String toString() {
      return "MoveEvent(vec=" + this.getVec3d() + ", stop=" + this.isFlag() + ")";
   }

   public boolean testObject(Object object) {
      return object instanceof MoveEvent2;
   }

   public double getDoubleValue2() {
      return this.vec3d.x;
   }

   public Vec3d getVec3d() {
      return this.vec3d;
   }
}
