package org.phobos.core.entity;

import net.minecraft.entity.Entity;

public class EntityAnimationState {
   public final Entity entity;
   public boolean flag;
   public float floatValue = 1.0F;
   public boolean flag2;
   public boolean flag3;

   public void setFlag3(boolean flag) {
      this.flag3 = flag;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag3() {
      return this.flag3;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public boolean isFlag2() {
      return this.flag2;
   }

   public EntityAnimationState(Entity entity) {
      this.entity = entity;
   }
}
