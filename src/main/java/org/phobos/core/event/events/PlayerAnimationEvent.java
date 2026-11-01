package org.phobos.core.event.events;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class PlayerAnimationEvent extends CancellableEvent {
   public final AbstractClientPlayerEntity abstractClientPlayerEntity;
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;
   public float floatValue5;
   public float floatValue6;

   public void setFloatValue3(float f) {
      this.floatValue = f;
   }

   public float getFloatValue3() {
      return this.floatValue;
   }

   public float getFloatValue2() {
      return this.floatValue5;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public void setFloatValue2(float f) {
      this.floatValue5 = f;
   }

   public float getFloatValue() {
      return this.floatValue2;
   }

   public PlayerAnimationEvent(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      this.abstractClientPlayerEntity = abstractClientPlayerEntity;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public void setFloatValue(float f) {
      this.floatValue2 = f;
   }

   public AbstractClientPlayerEntity getAbstractClientPlayerEntity() {
      return this.abstractClientPlayerEntity;
   }

   public float getFloatValue6() {
      return this.floatValue6;
   }

   public void setFloatValue6(float f) {
      this.floatValue6 = f;
   }

   public void setFloatValue32(float f) {
      this.floatValue3 = f;
   }

   public float getFloatValue32() {
      return this.floatValue3;
   }
}
