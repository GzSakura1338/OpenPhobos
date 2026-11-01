package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.render.CrosshairModule;

public final class CrosshairPostReceiveEntityDamageListener extends ModuleListener.Safe<CrosshairModule, PacketEvent.PostReceive<EntityDamageS2CPacket>> {
   public CrosshairPostReceiveEntityDamageListener(CrosshairModule crosshairModule) {
      super(crosshairModule, PacketEvent.PostReceive.class, EntityDamageS2CPacket.class);
   }

   public void handleObject(
      PacketEvent.PostReceive<EntityDamageS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      DamageSource damageSource = ((EntityDamageS2CPacket)postReceive.getPacket()).createDamageSource(clientWorld);
      if (damageSource.getAttacker() != null && damageSource.getAttacker().equals(clientPlayerEntity)) {
         if (this.module.sync.getValue()) {
            this.module.run();
         }
      }
   }
}
