package org.phobos.event;

import net.minecraft.block.StairsBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Position;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.module.movement.StairsModule;

public final class StairsPlayerTickListener extends ModuleListener.Safe<StairsModule, PlayerTickEvent> {
   public final MutableBlockPos mutableBlockPos = new MutableBlockPos();

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (clientPlayerEntity.getY() != this.module.doubleValue || this.module.normal.testLong(100L)) {
         if (this.module.doubleValue != this.module.doubleValue2) {
            this.module.doubleValue2 = this.module.doubleValue;
         }

         this.module.doubleValue = clientPlayerEntity.getY();
      }

      if (!clientPlayerEntity.isSpectator() && !clientPlayerEntity.isRiding() && !clientPlayerEntity.isHoldingOntoLadder() && !clientPlayerEntity.isSneaking()) {
         if (clientPlayerEntity.isOnGround() && !(clientPlayerEntity.forwardSpeed <= 0.0F)) {
            if (!(this.module.doubleValue2 < this.module.doubleValue)) {
               if (this.testClientWorld(clientWorld, clientPlayerEntity.getPos())) {
                  if (this.module.normal.testNumber2(this.module.delay.getValue())) {
                     clientPlayerEntity.jump();
                  }
               }
            }
         }
      }
   }

   public boolean testClientWorld(ClientWorld clientWorld, Position position) {
      this.mutableBlockPos.handlePosition(position);
      if (clientWorld.getBlockState(this.mutableBlockPos).getBlock() instanceof StairsBlock) {
         return true;
      }

      this.mutableBlockPos.set(position.getX(), position.getY() - 1.0, position.getZ());
      return clientWorld.getBlockState(this.mutableBlockPos).getBlock() instanceof StairsBlock;
   }

   public StairsPlayerTickListener(StairsModule stairsModule) {
      super(stairsModule, PlayerTickEvent.class);
   }
}
