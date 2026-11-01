package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.HighJumpModule;
import org.phobos.module.movement.HighJumpModule;

public final class HighJumpJumpListener extends ModuleListener.Safe<HighJumpModule, JumpEvent> {
   public void handleObject(
      JumpEvent jumpEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((HighJumpModule.Mode)this.module.mode.getValue()) {
         case Factor:
            jumpEvent.setFloatValue(jumpEvent.getFloatValue() * this.module.modifier.getValue());
            break;
         case Static:
            jumpEvent.setFloatValue(this.module.height.getValue());
      }

      if (this.module.mode.testObject2(HighJumpModule.Mode.Factor)) {
         jumpEvent.setFloatValue(this.module.modifier.getValue());
      }
   }

   public HighJumpJumpListener(HighJumpModule highJumpModule) {
      super(highJumpModule, JumpEvent.class);
   }
}
