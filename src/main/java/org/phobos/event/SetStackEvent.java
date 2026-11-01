package org.phobos.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SetStackEvent {
   public final PlayerEntity playerEntity;
   public final int intValue;
   public final ItemStack itemStack;

   public int getIntValue() {
      return this.intValue;
   }

   public SetStackEvent(PlayerEntity playerEntity, int i, ItemStack itemStack) {
      this.playerEntity = playerEntity;
      this.intValue = i;
      this.itemStack = itemStack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public PlayerEntity getPlayerEntity() {
      return this.playerEntity;
   }
}
