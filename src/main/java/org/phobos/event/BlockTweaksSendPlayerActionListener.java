package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.module.ModuleLookup;
import org.phobos.module.player.BlockTweaksModule;
import org.phobos.module.player.SpeedmineModule;

public final class BlockTweaksSendPlayerActionListener extends ModuleListener.Safe<BlockTweaksModule, PacketEvent.Send<PlayerActionC2SPacket>> {
   public void handleObject(
      PacketEvent.Send<PlayerActionC2SPacket> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.noBreakAnim.getValue()) {
         if (((PlayerActionC2SPacket)send.getPacket()).getAction().equals(Action.START_DESTROY_BLOCK)) {
            BlockPos blockPos = ((PlayerActionC2SPacket)send.getPacket()).getPos();
            BlockState blockState = clientWorld.getBlockState(blockPos);
            Direction direction = ((PlayerActionC2SPacket)send.getPacket()).getDirection();
            if (ModuleLookup.createModuleHandle(SpeedmineModule.class)
               .resolveObject(speedmineModule -> speedmineModule.testBlockPos(blockPos, blockState, clientPlayerEntity, clientWorld), false)) {
               clientPlayerEntity.networkHandler
                  .sendPacket(
                     new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, blockPos, direction, ((PlayerActionC2SPacket)send.getPacket()).getSequence() + 1)
                  );
            }
         }
      }
   }

   public BlockTweaksSendPlayerActionListener(BlockTweaksModule blockTweaksModule) {
      super(blockTweaksModule, PacketEvent.Send.class, PlayerActionC2SPacket.class);
   }
}
