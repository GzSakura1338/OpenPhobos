package org.phobos.event;

import net.minecraft.entity.LivingEntity;

public final class TrackedPositionUpdateEvent {
   public final double doubleValue;
   public final LivingEntity livingEntity;
   public final float floatValue;
   public final double doubleValue2;
   public final double doubleValue3;
   public final long longValue;
   public final float floatValue2;

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   @Override
   public String toString() {
      return "LerpToEvent(entity="
         + this.getLivingEntity()
         + ", x="
         + this.getDoubleValue3()
         + ", y="
         + this.getDoubleValue2()
         + ", z="
         + this.getDoubleValue()
         + ", yaw="
         + this.getFloatValue()
         + ", pitch="
         + this.getFloatValue2()
         + ", lastLerp="
         + this.getLongValue()
         + ")";
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof TrackedPositionUpdateEvent trackedPositionUpdateEvent)) {
         return false;
      } else {
         if (Double.compare(this.getDoubleValue3(), trackedPositionUpdateEvent.getDoubleValue3()) != 0) {
            return false;
         }

         if (Double.compare(this.getDoubleValue2(), trackedPositionUpdateEvent.getDoubleValue2()) != 0) {
            return false;
         }

         if (Double.compare(this.getDoubleValue(), trackedPositionUpdateEvent.getDoubleValue()) != 0) {
            return false;
         }

         if (Float.compare(this.getFloatValue(), trackedPositionUpdateEvent.getFloatValue()) != 0) {
            return false;
         }

         if (Float.compare(this.getFloatValue2(), trackedPositionUpdateEvent.getFloatValue2()) != 0) {
            return false;
         }

         if (this.getLongValue() != trackedPositionUpdateEvent.getLongValue()) {
            return false;
         }

         Object object2 = this.getLivingEntity();
         Object object3 = trackedPositionUpdateEvent.getLivingEntity();
         return object2 == null ? object3 == null : object2.equals(object3);
      }
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public TrackedPositionUpdateEvent(LivingEntity livingEntity, double d, double d2, double d3, float f, float f2, long l) {
      this.livingEntity = livingEntity;
      this.doubleValue3 = d;
      this.doubleValue2 = d2;
      this.doubleValue = d3;
      this.floatValue = f;
      this.floatValue2 = f2;
      this.longValue = l;
   }

   @Override
   public int hashCode() {
      int i = 59;
      int i2 = 1;
      long l = Double.doubleToLongBits(this.getDoubleValue3());
      i2 = i2 * 59 + (int)(l >>> 32 ^ l);
      long l2 = Double.doubleToLongBits(this.getDoubleValue2());
      i2 = i2 * 59 + (int)(l2 >>> 32 ^ l2);
      long l3 = Double.doubleToLongBits(this.getDoubleValue());
      i2 = i2 * 59 + (int)(l3 >>> 32 ^ l3);
      i2 = i2 * 59 + Float.floatToIntBits(this.getFloatValue());
      i2 = i2 * 59 + Float.floatToIntBits(this.getFloatValue2());
      long l4 = this.getLongValue();
      i2 = i2 * 59 + (int)(l4 >>> 32 ^ l4);
      Object object = this.getLivingEntity();
      return i2 * 59 + (object == null ? 43 : object.hashCode());
   }
}
