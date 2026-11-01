package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.HitSoundModule;

public final class HitSoundPostReceiveEntityDamageListener extends ModuleListener.Safe<HitSoundModule, PacketEvent.PostReceive<EntityDamageS2CPacket>> {
   public void handleObject(
      PacketEvent.PostReceive<EntityDamageS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.sync.getValue()) {
         DamageSource damageSource = ((EntityDamageS2CPacket)postReceive.getPacket()).createDamageSource(clientWorld);
         if (damageSource.getAttacker() != null && damageSource.getAttacker().equals(clientPlayerEntity)) {
            this.module.mode.getValue().play(this.phobosClient, this.module.volume.getValue());
         }
      }
   }

   public HitSoundPostReceiveEntityDamageListener(HitSoundModule hitSoundModule) {
      super(hitSoundModule, PacketEvent.PostReceive.class, EntityDamageS2CPacket.class);
   }
}
