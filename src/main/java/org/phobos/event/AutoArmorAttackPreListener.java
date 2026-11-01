package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.module.combat.AutoArmorModule;

public final class AutoArmorAttackPreListener extends ModuleListener.Safe<AutoArmorModule, AttackEvent2.Pre> {
   public AutoArmorAttackPreListener(AutoArmorModule autoArmorModule) {
      super(autoArmorModule, AttackEvent2.Pre.class);
   }

   public void handleObject(
      AttackEvent2.Pre pre, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.booleanValueSetting4.getValue() && this.module.booleanValueSetting3.getValue() && this.module.flag3) {
         MutableBoolean mutableBoolean = new MutableBoolean();
         this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
            Slot slot = itemSwitcher.getSlot3();
            if (InventoryUtils.testItemStack2(slot.getStack())) {
               mutableBoolean.setValue(true);
            }
         });
         if (mutableBoolean.getValue()) {
            this.module.run5();
            this.module.handleClientPlayerEntity3(clientPlayerEntity, clientWorld);
         }
      }
   }
}
