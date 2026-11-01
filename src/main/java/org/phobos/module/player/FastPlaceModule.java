package org.phobos.module.player;

import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.SlotSwap;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SafeListener;
import org.phobos.mixin.IMinecraftClient;
import org.phobos.module.Module;

public class FastPlaceModule extends Module {
   public final Setting<Boolean> any;
   public final Setting<Boolean> compute;
   public final Setting<Set<Item>> whitelist;
   public static FastPlaceModule fastPlaceModule;
   public final Setting<Integer> delay = this.resolveSetting2(new NumberSetting<>("Delay", 1, 0, 10));

   public FastPlaceModule(PhobosClient phobosClient) {
      super(phobosClient, "FastPlace", Categories.player, "Allows for faster interactions");
      this.whitelist = this.resolveSetting2(new ItemSetSetting("Whitelist"));
      this.any = this.resolveSetting2(new BooleanSetting("Any"));
      this.compute = this.resolveSetting2(new BooleanSetting("Compute").resolveSetting7("Updates delay after item switch"));
      this.handleListener(
         new SafeListener<SlotSwap>() {
            public void handleObject(
               SlotSwap slotSwap,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (FastPlaceModule.this.compute.getValue()) {
                  ((IMinecraftClient)(Object)mc).setItemUseCooldown(FastPlaceModule.computeItemStack(slotSwap.getSlot().getStack(), Hand.MAIN_HAND));
               }
            }
         }
      );
      fastPlaceModule = this;
   }

   public static int computeItemStack(@NotNull ItemStack itemStack, @NotNull Hand hand) {
      if (!fastPlaceModule.check7()) {
         return 4;
      } else if (hand != Hand.MAIN_HAND) {
         return mc.player == null ? 0 : computeItemStack(mc.player.getMainHandStack(), Hand.MAIN_HAND);
      } else {
         return !fastPlaceModule.whitelist.getValue().contains(itemStack.getItem()) && !fastPlaceModule.any.getValue() ? 4 : fastPlaceModule.delay.getValue();
      }
   }
}
