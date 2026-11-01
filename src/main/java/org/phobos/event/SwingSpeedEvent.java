package org.phobos.event;

import net.minecraft.entity.LivingEntity;

public class SwingSpeedEvent {
   public int intValue;
   public final LivingEntity livingEntity;

   public SwingSpeedEvent(LivingEntity livingEntity, int i) {
      this.livingEntity = livingEntity;
      this.intValue = i;
   }

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public int getIntValue() {
      return this.intValue;
   }
}
