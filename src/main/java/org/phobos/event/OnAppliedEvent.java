package org.phobos.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

public class OnAppliedEvent {
   public final LivingEntity livingEntity;
   public final int intValue;
   public final StatusEffect statusEffect;

   public int getIntValue() {
      return this.intValue;
   }

   public OnAppliedEvent(LivingEntity livingEntity, StatusEffect statusEffect, int i) {
      this.livingEntity = livingEntity;
      this.statusEffect = statusEffect;
      this.intValue = i;
   }

   public StatusEffect getStatusEffect() {
      return this.statusEffect;
   }

   public LivingEntity getLivingEntity() {
      return this.livingEntity;
   }
}
