package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShapes;
import org.phobos.core.block.VoxelShapeUtils;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.core.block.BlockUtils;
import org.phobos.module.movement.PhaseModule;
import org.phobos.module.movement.PhaseModule;

public final class PhaseComputeNextListener extends ModuleListener.Safe<PhaseModule, ComputeNextEvent> {
   public PhaseComputeNextListener(PhaseModule phaseModule) {
      super(phaseModule, ComputeNextEvent.class);
   }

   public void handleObject(
      ComputeNextEvent computeNextEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((PhaseModule.Mode)this.module.mode.getValue()) {
         case Normal:
            Box box = new Box(computeNextEvent.getBlockPos());
            if (!mc.options.jumpKey.isPressed()) {
               return;
            }

            if (box.maxY <= clientPlayerEntity.getBoundingBox().minY) {
               return;
            }

            if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               return;
            }

            computeNextEvent.setCancelled(true);
            break;
         case Anti:
            if (!clientPlayerEntity.isOnGround()) {
               return;
            }

            BlockPos blockPos = computeNextEvent.getBlockPos();
            if (blockPos.getY() < clientPlayerEntity.getPos().getY()) {
               return;
            }

            if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               return;
            }

            if (!computeNextEvent.getBlockState().isAir()) {
               return;
            }

            computeNextEvent.setVoxelShape(PhaseModule.voxelShape);
            break;
         case Flow:
            if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               return;
            }

            if (!clientPlayerEntity.input.sneaking && !clientPlayerEntity.input.jumping) {
               return;
            }

            if (computeNextEvent.getBlockPos().getY() == -64) {
               return;
            }

            Box box2 = new Box(computeNextEvent.getBlockPos());
            if (box2.maxY <= clientPlayerEntity.getBoundingBox().minY && PlayerUtils.check5()) {
               return;
            }

            computeNextEvent.setCancelled(true);
            break;
         case Sand:
            computeNextEvent.setCancelled(true);
            break;
         case Radon_Auto:
            Box box3 = new Box(computeNextEvent.getBlockPos());
            if (box3.maxY <= clientPlayerEntity.getBoundingBox().minY && !mc.options.sneakKey.isPressed()) {
               return;
            }

            computeNextEvent.setCancelled(true);
            break;
         case Correction:
            if (!BlockUtils.testPlayerEntity(clientPlayerEntity, clientWorld)) {
               return;
            }

            if (mc.options.sneakKey.isPressed()) {
               return;
            }

            if (computeNextEvent.getVoxelShape().isEmpty()) {
               return;
            }

            if (computeNextEvent.getBlockPos().getY() < clientPlayerEntity.getBlockPos().getY()) {
               return;
            }

            BlockPos blockPos2 = computeNextEvent.getBlockPos();
            if (!BlockPosUtils.resolveSet6(clientPlayerEntity.getBoundingBox().expand(-0.1, 0.0, -0.1)).contains(blockPos2)) {
               return;
            }

            double d = computeNextEvent.getVoxelShape().getMax(Axis.Y);
            Box box4 = new Box(0.4999, 0.0, 0.4999, 0.5111, d, 0.5111);
            computeNextEvent.setVoxelShape(VoxelShapes.cuboid(box4));
            break;
         case Polar:
            if (computeNextEvent.getBlockPos().getY() >= clientPlayerEntity.getPos().getY()
               || clientPlayerEntity.isSneaking() && clientPlayerEntity.isOnGround()) {
               computeNextEvent.setVoxelShape(VoxelShapeUtils.resolveVoxelShape(computeNextEvent.getVoxelShape(), 1.0E-4, 0.0, 1.0E-4));
            }
      }
   }
}
