package org.phobos.event;

import net.minecraft.entity.LivingEntity;

public class SetEvent {
   public final float floatValue;
   public final float floatValue2;
   public final LivingEntity livingEntity;

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public SetEvent(LivingEntity livingEntity, float f, float f2) {
      this.livingEntity = livingEntity;
      this.floatValue2 = f;
      this.floatValue = f2;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }
}
