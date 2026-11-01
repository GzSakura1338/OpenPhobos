package org.phobos.event;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;

public class FindCrosshairTargetEvent {
   public final Entity entity;
   public final double doubleValue;
   public final float floatValue;
   public final double doubleValue2;
   public HitResult hitResult;

   public FindCrosshairTargetEvent(HitResult hitResult, Entity entity, double d, double d2, float f) {
      this.hitResult = hitResult;
      this.entity = entity;
      this.doubleValue2 = d;
      this.doubleValue = d2;
      this.floatValue = f;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public HitResult getHitResult() {
      return this.hitResult;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setHitResult(HitResult hitResult) {
      this.hitResult = hitResult;
   }
}
