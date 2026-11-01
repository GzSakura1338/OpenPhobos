package org.phobos.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.phobos.core.inventory.SilentHotbarManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class SilentHotbarModule extends Module {
   public static SilentHotbarModule silentHotbarModule;

   public static ItemStack resolveItemStack(ClientPlayerEntity clientPlayerEntity) {
      return getSilentHotbarModule().check7()
         ? (ItemStack)clientPlayerEntity.getInventory().main.get(getSilentHotbarManager().value2())
         : clientPlayerEntity.getMainHandStack();
   }

   public static int computeInt(int i) {
      return getSilentHotbarManager().computeInt(i);
   }

   public static int value() {
      return getSilentHotbarManager().value();
   }

   public static SilentHotbarModule getSilentHotbarModule() {
      return silentHotbarModule;
   }

   public static SilentHotbarManager getSilentHotbarManager() {
      return SilentHotbarManager.silentHotbarManager;
   }

   public SilentHotbarModule(PhobosClient phobosClient) {
      super(phobosClient, "SilentHotbar", Categories.player);
      silentHotbarModule = this;
   }

   public static ItemStack resolveItemStack2(ItemStack itemStack) {
      return getSilentHotbarModule().check7() && mc.player != null ? (ItemStack)mc.player.getInventory().main.get(getSilentHotbarManager().value2()) : itemStack;
   }
}
