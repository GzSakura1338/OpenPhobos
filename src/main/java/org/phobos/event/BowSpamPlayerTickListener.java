package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.phobos.module.combat.BowSpamModule;

public final class BowSpamPlayerTickListener extends ModuleListener.Safe<BowSpamModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (clientPlayerEntity.isUsingItem()) {
         Hand hand = clientPlayerEntity.getActiveHand();
         ItemStack itemStack = clientPlayerEntity.getStackInHand(hand);
         if (itemStack.isOf(Items.BOW)) {
            if (clientPlayerEntity.getItemUseTime() >= this.module.ticks.getValue()) {
               clientPlayerInteractionManager.stopUsingItem(clientPlayerEntity);
            }
         }
      }
   }

   public BowSpamPlayerTickListener(BowSpamModule bowSpamModule) {
      super(bowSpamModule, PlayerTickEvent.class);
   }
}
