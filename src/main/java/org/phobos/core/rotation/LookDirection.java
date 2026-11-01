package org.phobos.core.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.tick.TimeUtils;

public class LookDirection extends Vec3d {
   public final float floatValue;
   public final boolean flag;
   public final float floatValue2;
   public final long longValue = TimeUtils.value();

   public LookDirection() {
      this(0.0, 0.0, 0.0, 0.0F, 0.0F, false);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof LookDirection lookDirection)) {
         return false;
      } else if (!lookDirection.testObject(this)) {
         return false;
      } else if (!super.equals(object)) {
         return false;
      } else if (Float.compare(this.getFloatValue(), lookDirection.getFloatValue()) != 0) {
         return false;
      } else {
         return Float.compare(this.getFloatValue2(), lookDirection.getFloatValue2()) != 0 ? false : this.isFlag() == lookDirection.isFlag();
      }
   }

   public int hashCode() {
      int i = 59;
      int i2 = super.hashCode();
      i2 = i2 * 59 + Float.floatToIntBits(this.getFloatValue());
      i2 = i2 * 59 + Float.floatToIntBits(this.getFloatValue2());
      return i2 * 59 + (this.isFlag() ? 79 : 97);
   }

   public LookDirection(double d, double d2, double d3, float f, float f2, boolean flag) {
      super(d, d2, d3);
      this.floatValue = f2;
      this.floatValue2 = f;
      this.flag = flag;
   }

   public boolean testObject(Object object) {
      return object instanceof LookDirection;
   }

   public LookDirection(LookDirection lookDirection, float f, float f2, boolean flag) {
      this(lookDirection.getX(), lookDirection.getY(), lookDirection.getZ(), f, f2, flag);
   }

   public LookDirection(LookDirection lookDirection, double d, double d2, double d3, boolean flag) {
      this(d, d2, d3, lookDirection.getFloatValue2(), lookDirection.getFloatValue(), flag);
   }

   public LookDirection(LookDirection lookDirection, boolean flag) {
      this(lookDirection.getX(), lookDirection.getY(), lookDirection.getZ(), lookDirection.getFloatValue2(), lookDirection.getFloatValue(), flag);
   }

   public long getLongValue() {
      return this.longValue;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void handleEntity(Entity entity) {
      entity.setPosition(this.getX(), this.getY(), this.getZ());
      entity.setPitch(this.getFloatValue2());
      entity.setYaw(this.getFloatValue());
      entity.setOnGround(this.isFlag());
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
