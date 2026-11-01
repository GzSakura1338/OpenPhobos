package org.phobos.event;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.mixin.net.interfaces.IPlayerPositionLookS2CPacket;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;
import org.phobos.module.movement.PacketFlyModule;

public final class PacketFlyReceivePlayerPositionLookListener extends ModuleListener.Safe<PacketFlyModule, PacketEvent.Receive<PlayerPositionLookS2CPacket>> {
   public PacketFlyReceivePlayerPositionLookListener(PacketFlyModule packetFlyModule) {
      super(packetFlyModule, PacketEvent.Receive.class, PlayerPositionLookS2CPacket.class, Integer.MAX_VALUE);
   }

   public void handleObject(
      PacketEvent.Receive<PlayerPositionLookS2CPacket> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      double d = ((PlayerPositionLookS2CPacket)receive.getPacket()).getX();
      double d2 = ((PlayerPositionLookS2CPacket)receive.getPacket()).getY();
      double d3 = ((PlayerPositionLookS2CPacket)receive.getPacket()).getZ();
      BlockPos blockPos = clientPlayerEntity.getBlockPos();
      if (clientPlayerEntity.isAlive()
         && this.module.mode.getValue() != PacketFlyModule.Mode.Jitter
         && clientWorld.isChunkLoaded((int)clientPlayerEntity.getX() >> 4, (int)clientPlayerEntity.getZ() >> 4)
         && !(mc.currentScreen instanceof DownloadingTerrainScreen)) {
         PacketFlyModule.Instance instance = this.module.integerMap.remove(((PlayerPositionLookS2CPacket)receive.getPacket()).getTeleportId());
         if (instance != null) {
            Vec3d vec3d = instance.pos();
            if (vec3d.getX() == d && vec3d.getY() == d2 && vec3d.getZ() == d3) {
               receive.setCancelled(true);
               return;
            }
         }
      }

      this.module.atomicInteger.set(((PlayerPositionLookS2CPacket)receive.getPacket()).getTeleportId());
      IPlayerPositionLookS2CPacket iPlayerPositionLookS2CPacket = (IPlayerPositionLookS2CPacket)(Object)receive.getPacket();
      iPlayerPositionLookS2CPacket.setYaw(clientPlayerEntity.getYaw());
      iPlayerPositionLookS2CPacket.setPitch(clientPlayerEntity.getPitch());
   }
}
