package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;

public final class DisablerPlayerTickListener extends ModuleListener.Safe<DisablerModule, PlayerTickEvent> {
   public DisablerPlayerTickListener(DisablerModule disablerModule) {
      super(disablerModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((DisablerModule.Mode)this.module.mode.getValue()) {
         case Vulcan:
            if (clientPlayerEntity.isInFluid()
               || clientPlayerEntity.isTouchingWater()
               || clientPlayerEntity.isDead()
               || clientPlayerEntity.isHoldingOntoLadder()
               || clientPlayerEntity.getAbilities().flying) {
               return;
            }

            clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.START_SPRINTING));
            clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.STOP_SPRINTING));
            if (this.module.normal.testNumber2(this.module.delay.getValue()) && clientPlayerEntity.isOnGround()) {
               clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.RELEASE_SHIFT_KEY));
            }
            break;
         case Grim:
            BlockPos blockPos = BlockPos.ofFloored(clientPlayerEntity.getPos());
            BlockPos blockPos2 = blockPos.up();
            BlockState blockState;
            if (!(blockState = clientWorld.getBlockState(blockPos)).isAir() && !clientWorld.getBlockState(blockPos2).isAir()) {
               return;
            }

            BlockPos blockPos3 = blockState.isAir() ? blockPos : blockPos2;
            clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos3, Direction.DOWN));
            break;
         case Grim_Full:
            if (!this.phobosClient.getTickManager().testLong(100L)) {
               this.module.flag6 = false;
               this.module.intValue5 = -1;
            }

            if (!this.module.flag6) {
               for (int i = 0; i < 16; i++) {
                  PlayerActionUtils.handleClientPlayerEntity17(clientPlayerEntity, 174.09172F, 8.75762E-4F, false);
               }

               this.module.flag6 = true;
               this.module.intValue5 = 256;
            }

            float f = MathUtils.computeFloat8(-0.14772F, 0.130086F);
            float f2 = this.module.floatValue > 0.0F ? -45.40445F : 50.055893F;
            this.module.floatValue = f2;
            if (this.phobosClient.getRotationManager().testFloat(f, f2)) {
               this.module.intValue5++;
            }
            break;
         case Polar:
            if (this.module.normal.testNumber2(this.module.delay.getValue())) {
               Box box = clientPlayerEntity.getBoundingBox().offset(0.0, -1.0, 0.0).stretch(0.0, -0.5, 0.0);
               BlockPos.stream(box)
                  .filter(blockPosArg -> !clientWorld.getBlockState(blockPosArg).isAir())
                  .forEach(
                     blockPosArg -> {
                        clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPosArg, Direction.UP));
                        clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.PRESS_SHIFT_KEY));
                        this.phobosClient
                           .getDelayedTaskManager()
                           .handleRunnable(
                              () -> clientPlayerEntity.networkHandler.sendPacket(new ClientCommandC2SPacket(clientPlayerEntity, Mode.RELEASE_SHIFT_KEY)), 1L
                           );
                     }
                  );
            }
            break;
         case Matrix:
            this.phobosClient.getElytraManager().testFlyConfiguration2(this.module.flyConfiguration);
      }
   }
}
