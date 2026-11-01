package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.player.AutoFireworkModule;

public final class AutoFireworkPostSendClientCommandListener extends ModuleListener.Safe<AutoFireworkModule, PacketEvent.PostSend<ClientCommandC2SPacket>> {
   public AutoFireworkPostSendClientCommandListener(AutoFireworkModule autoFireworkModule) {
      super(autoFireworkModule, PacketEvent.PostSend.class, ClientCommandC2SPacket.class);
   }

   public void handleObject(
      PacketEvent.PostSend<ClientCommandC2SPacket> postSend,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (((ClientCommandC2SPacket)postSend.getPacket()).getMode().equals(Mode.START_FALL_FLYING)) {
         if (!this.phobosClient.getElytraManager().check2()) {
            if (this.module.normal.testNumber2(this.module.delay.getValue())) {
               this.phobosClient
                  .getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        Item item = itemSwitcher.resolveObject(
                           slot -> slot.getStack().getItem() instanceof FireworkRocketItem fireworkRocketItem
                                 && this.module.testFireworkRocketItem(fireworkRocketItem)
                              ? slot.getStack().getItem()
                              : null
                        );
                        if (item != null) {
                           itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, this.module.switchValue.getValue().getFlag() | 1);
                           TickUtils.handleClientWorld(
                              clientWorld,
                              integer -> clientPlayerEntity.networkHandler
                                 .sendPacket(
                                    new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch())
                                 )
                           );
                        }
                     }
                  );
            }
         }
      }
   }
}
