package org.phobos.event;

import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AntiLagModule;

public final class AntiLagReceiveEntitySpawnListener extends ModuleListener.Safe<AntiLagModule, PacketEvent.Receive<EntitySpawnS2CPacket>> {
   public AntiLagReceiveEntitySpawnListener(AntiLagModule antiLagModule) {
      super(antiLagModule, PacketEvent.Receive.class, EntitySpawnS2CPacket.class, Integer.MAX_VALUE);
   }

   public void handleObject(
      PacketEvent.Receive<EntitySpawnS2CPacket> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      EntitySpawnS2CPacket entitySpawnS2CPacket = (EntitySpawnS2CPacket)receive.getPacket();
      if (this.module.entityFix.getValue()) {
         Predicate<EntitySpawnS2CPacket> predicate = entitySpawnS2CPacketx -> entitySpawnS2CPacketx.getEntityType().equals(EntityType.ENDER_PEARL)
            || entitySpawnS2CPacketx.getEntityType().equals(EntityType.EXPERIENCE_BOTTLE);
         if (!clientWorld.getWorldBorder().contains(entitySpawnS2CPacket.getX(), entitySpawnS2CPacket.getY(), entitySpawnS2CPacket.getZ())
            && predicate.test(entitySpawnS2CPacket)) {
            receive.setCancelled(true);
         }
      }

      if (this.module.itemFix.getValue() && entitySpawnS2CPacket.getEntityType().equals(EntityType.ITEM)) {
         this.module.normal.run();
         if (++this.module.intValue > 100) {
            receive.setCancelled(true);
         }
      }
   }
}
