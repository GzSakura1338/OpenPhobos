package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.movement.FastSwimModule;
import org.phobos.module.movement.FastSwimModule;

public final class FastSwimMoveListener extends ModuleListener.Safe<FastSwimModule, MoveEvent2> {
   public void handleObject(
      MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((FastSwimModule.Mode)this.module.mode.getValue()) {
         case Vanilla:
            if (!this.module.testClientPlayerEntity(clientPlayerEntity)) {
               return;
            }

            if (this.module.horizontal.getValue()) {
               float[] floats = PlayerUtils.values3(clientPlayerEntity, this.module.horizontalSpeed.getValue());
               moveEvent2.setDoubleValue3(floats[0]);
               moveEvent2.setDoubleValue2(floats[1]);
            }

            if (this.module.vertical.getValue()) {
               int i = PlayerUtils.computeFlag(clientPlayerEntity.input.jumping, clientPlayerEntity.input.sneaking);
               if (i == 0) {
                  return;
               }

               moveEvent2.setDoubleValue(this.module.verticalSpeed.getValue() * i);
            }
            break;
         case Intave:
            if (this.module.testClientPlayerEntity(clientPlayerEntity)) {
               double[] doubles = PlayerUtils.values5(this.module.floatValue / (clientPlayerEntity.input.movementSideways != 0.0F ? 2.2F : 2.0F));
               moveEvent2.setDoubleValue3(moveEvent2.getDoubleValue2() + doubles[0]);
               moveEvent2.setDoubleValue2(moveEvent2.getDoubleValue() + doubles[1]);
               this.module.floatValue += 0.05F;
               this.module.floatValue = MathUtils.computeFloat3(this.module.floatValue, 0.0F, 1.0F);
            } else {
               this.module.floatValue = 0.0F;
            }

            if (!PlayerUtils.check()) {
               this.module.floatValue = 0.0F;
            }
      }
   }

   public FastSwimMoveListener(FastSwimModule fastSwimModule) {
      super(fastSwimModule, MoveEvent2.class);
   }
}
