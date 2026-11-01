package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.JesusModule;

public final class JesusJumpListener extends ModuleListener.Safe<JesusModule, JumpEvent3> {
   public void handleObject(
      JumpEvent3 jumpEvent3, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      jumpEvent3.setCancelled(this.module.testClientPlayerEntity(clientPlayerEntity, clientWorld));
   }

   public JesusJumpListener(JesusModule jesusModule) {
      super(jesusModule, JumpEvent3.class);
   }
}
