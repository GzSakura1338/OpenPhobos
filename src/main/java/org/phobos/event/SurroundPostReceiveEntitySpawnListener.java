package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.earthhack.ducks.IEntity;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.tick.TimeUtils;
import org.phobos.module.combat.SurroundModule;

public final class SurroundPostReceiveEntitySpawnListener extends ModuleListener.Safe<SurroundModule, PacketEvent.PostReceive<EntitySpawnS2CPacket>> {
   public SurroundPostReceiveEntitySpawnListener(SurroundModule surroundModule) {
      super(surroundModule, PacketEvent.PostReceive.class, EntitySpawnS2CPacket.class, 99);
   }

   public void handleObject(
      PacketEvent.PostReceive<EntitySpawnS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (((EntitySpawnS2CPacket)postReceive.getPacket()).getEntityType().equals(EntityType.END_CRYSTAL)) {
         Entity entity = clientWorld.getEntityById(((EntitySpawnS2CPacket)postReceive.getPacket()).getEntityId());
         if (entity instanceof IEntity iEntity
            && TimeUtils.testLong2(iEntity.getAttackTime(), 100L)
            && entity.getBoundingBox().squaredMagnitude(clientPlayerEntity.getEyePos()) < 6.0) {
            for (BlockPos blockPos : this.module.blockPosSet) {
               if (entity.getBoundingBox().intersects(new Box(blockPos))) {
                  this.module.handleClientPlayerEntity5(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
               }
            }
         }
      }
   }
}
