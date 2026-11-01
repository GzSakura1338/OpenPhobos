package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.PhaseModule;
import org.phobos.module.movement.PhaseModule;

public final class PhaseJumpListener extends ModuleListener.Safe<PhaseModule, JumpEvent> {
   public void handleObject(
      JumpEvent jumpEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((PhaseModule.Mode)this.module.mode.getValue()) {
         case Polar:
            if (clientPlayerEntity.horizontalCollision) {
               jumpEvent.setFloatValue(0.55F);
            }
      }
   }

   public PhaseJumpListener(PhaseModule phaseModule) {
      super(phaseModule, JumpEvent.class);
   }
}
