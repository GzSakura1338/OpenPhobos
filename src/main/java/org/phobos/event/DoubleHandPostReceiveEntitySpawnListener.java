package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.combat.DoubleHandModule;

public final class DoubleHandPostReceiveEntitySpawnListener extends ModuleListener.Safe<DoubleHandModule, PacketEvent.PostReceive<EntitySpawnS2CPacket>> {
   public void handleObject(
      PacketEvent.PostReceive<EntitySpawnS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(DoubleHandModule.Mode.Legit)
         && ((EntitySpawnS2CPacket)postReceive.getPacket()).getEntityType() == EntityType.END_CRYSTAL) {
         double d = ((EntitySpawnS2CPacket)postReceive.getPacket()).getX();
         double d2 = ((EntitySpawnS2CPacket)postReceive.getPacket()).getY();
         double d3 = ((EntitySpawnS2CPacket)postReceive.getPacket()).getZ();
         EndCrystalEntity endCrystalEntity = new EndCrystalEntity(clientWorld, d, d2, d3);
         if (!PlayerActionUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, endCrystalEntity.getPos())) {
            if (!this.module.ground.getValue() || !clientPlayerEntity.isOnGround() || clientPlayerEntity.getBlockY() >= endCrystalEntity.getBlockY()) {
               float f = this.phobosClient.getDamageCalculator().computeEntity8(clientPlayerEntity, clientWorld, endCrystalEntity);
               if (f >= EntityUtils.computeLivingEntity(clientPlayerEntity) - this.module.threshold.getValue()) {
                  this.module.threaded.run();
               }
            }
         }
      }
   }

   public DoubleHandPostReceiveEntitySpawnListener(DoubleHandModule doubleHandModule) {
      super(doubleHandModule, PacketEvent.PostReceive.class, EntitySpawnS2CPacket.class);
   }
}
