package org.earthhack.ducks;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractTypeHandler;
import net.minecraft.world.World;

public interface IPlayerInteractEntityC2SPacket {
   Entity entity();

   void setEntity(Entity var1);

   boolean attack();

   void setAttack(boolean var1);

   int getEntityId();

   InteractTypeHandler getType();

   default Entity getEntityById(World world) {
      return world.getEntityById(this.getEntityId());
   }
}
