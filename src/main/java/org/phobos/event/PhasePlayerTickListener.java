package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.module.render.FreeLookModule;
import org.phobos.module.movement.PhaseModule;
import org.phobos.module.movement.PhaseModule;

public final class PhasePlayerTickListener extends ModuleListener.Safe<PhaseModule, PlayerTickEvent> {
   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, double d, float f, int i, int i2) {
      double d2 = Math.cos(d) * f * i;
      double d3 = Math.sin(d) * f * i2;
      clientPlayerEntity.setPosition(clientPlayerEntity.getX() + d2, clientPlayerEntity.getY(), clientPlayerEntity.getZ() + d3);
   }

   public PhasePlayerTickListener(PhaseModule phaseModule) {
      super(phaseModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((PhaseModule.Mode)this.module.mode.getValue()) {
         case Normal:
            if (clientPlayerEntity.input.sneaking && BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               float f = clientPlayerEntity.getYaw();
               double d = this.module.distance.getValue() * Math.cos(Math.toRadians(f + 90.0F));
               double d2 = this.module.distance.getValue() * Math.sin(Math.toRadians(f + 90.0F));
               Box box = clientPlayerEntity.getBoundingBox().offset(d, 0.0, d2);
               clientPlayerEntity.setBoundingBox(box);
            }
            break;
         case Sand:
            int i = clientPlayerEntity.input.jumping && clientPlayerEntity.input.sneaking ? 0 : (clientPlayerEntity.input.jumping ? 1 : -1);
            PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.3 * i);
            clientPlayerEntity.noClip = true;
            break;
         case Intave:
            if (this.module.auto.getValue() && !RotationManager.isFlag()) {
               this.phobosClient.getRotationManager().testFloat(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), 85.0F);
               mc.options.attackKey.setPressed(true);
            }

            boolean flag = mc.options.attackKey.isPressed() && this.phobosClient.getRotationManager().computeFloat6(clientPlayerEntity.getPitch()) > 80.0F;
            BlockPos blockPos = clientPlayerEntity.getBlockPos().add(0, -1, 0);
            if (flag) {
               clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
               this.module.flag2 = true;
            } else {
               this.module.flag2 = false;
            }

            if (this.module.flag2) {
               clientPlayerEntity.setPosition(clientPlayerEntity.getX(), clientPlayerEntity.getY() - 0.0052, clientPlayerEntity.getZ());
            }

            if (clientPlayerEntity.isSneaking()) {
               float f2 = 0.005F;
               double d3 = Math.toRadians(clientPlayerEntity.getYaw());
               if (mc.options.forwardKey.isPressed()) {
                  this.handleClientPlayerEntity(clientPlayerEntity, d3, f2, 1, 1);
               }

               if (mc.options.backKey.isPressed()) {
                  this.handleClientPlayerEntity(clientPlayerEntity, d3, -f2, 1, -1);
               }

               if (mc.options.leftKey.isPressed()) {
                  this.handleClientPlayerEntity(clientPlayerEntity, d3, f2, -1, 1);
               }

               if (mc.options.rightKey.isPressed()) {
                  this.handleClientPlayerEntity(clientPlayerEntity, d3, -f2, -1, -1);
               }
            }
      }
   }
}
