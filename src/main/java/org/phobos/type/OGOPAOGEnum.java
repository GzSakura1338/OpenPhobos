package org.phobos.type;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import org.phobos.core.ClientHolder;

public enum OGOPAOGEnum {
   OGO("OnGroundOnly") {
      @Override
      public PlayerMoveC2SPacket create(ClientPlayerEntity player, boolean useLast) {
         PlayerEntity lastPlayer = this.getPlayer(player, useLast);
         return new OnGroundOnly(lastPlayer.isOnGround());
      }
   },
   PAOG("PositionAndOnGround") {
      @Override
      public PlayerMoveC2SPacket create(ClientPlayerEntity player, boolean useLast) {
         PlayerEntity lastPlayer = this.getPlayer(player, useLast);
         return new PositionAndOnGround(lastPlayer.getX(), lastPlayer.getY(), lastPlayer.getZ(), lastPlayer.isOnGround());
      }
   },
   LAOG("LookAndOnGround") {
      @Override
      public PlayerMoveC2SPacket create(ClientPlayerEntity player, boolean useLast) {
         PlayerEntity lastPlayer = this.getPlayer(player, useLast);
         return new LookAndOnGround(lastPlayer.getYaw(), lastPlayer.getPitch(), lastPlayer.isOnGround());
      }
   },
   F("FULL") {
      @Override
      public PlayerMoveC2SPacket create(ClientPlayerEntity player, boolean useLast) {
         PlayerEntity lastPlayer = this.getPlayer(player, useLast);
         return new Full(lastPlayer.getX(), lastPlayer.getY(), lastPlayer.getZ(), lastPlayer.getYaw(), lastPlayer.getPitch(), lastPlayer.isOnGround());
      }
   };

   public final String description;

   public PlayerEntity getPlayer(ClientPlayerEntity player, boolean useLast) {
      return (PlayerEntity)(useLast ? ClientHolder.getPhobosClient().getMovementSimulator().resolvePlayerEntity(player) : player);
   }

   public abstract PlayerMoveC2SPacket create(ClientPlayerEntity var1, boolean var2);

   public String getDescription() {
      return this.description;
   }

   OGOPAOGEnum(final String description) {
      this.description = description;
   }
}
