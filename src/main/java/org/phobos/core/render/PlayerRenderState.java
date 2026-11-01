package org.phobos.core.render;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerRenderState {
   public float floatValue = Float.NaN;
   public final PlayerEntity playerEntity;
   public float floatValue2 = 1.0F;
   public long longValue;

   public PlayerRenderState(PlayerEntity playerEntity) {
      this.playerEntity = playerEntity;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }
}
