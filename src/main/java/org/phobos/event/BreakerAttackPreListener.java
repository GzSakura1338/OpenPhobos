package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.screen.slot.Slot;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.combat.BreakerModule;
import org.phobos.module.player.SilentHotbarModule;

public final class BreakerAttackPreListener extends ModuleListener.Safe<BreakerModule, AttackEvent2.Pre> {
   public BreakerAttackPreListener(BreakerModule breakerModule) {
      super(breakerModule, AttackEvent2.Pre.class);
   }

   public void handleObject(
      AttackEvent2.Pre pre, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(BreakerModule.Mode.Shield) && this.module.manual.getValue()) {
         if (pre.getEntity() instanceof LivingEntity livingEntity && livingEntity.isBlocking()) {
            this.phobosClient
               .getInventoryManager()
               .handleConsumer2(
                  itemSwitcher -> {
                     Slot slot = itemSwitcher.resolveObject(
                        slotx -> itemSwitcher.testSlot4(slotx) && slotx.getStack().getItem() instanceof AxeItem ? slotx : null
                     );
                     if (slot != null) {
                        SilentHotbarModule.getSilentHotbarManager()
                           .handleObject3(
                              this.module, slot, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig()
                           );
                     }
                  }
               );
         }
      }
   }
}
