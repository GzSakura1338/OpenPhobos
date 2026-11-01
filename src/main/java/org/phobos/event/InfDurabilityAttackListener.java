package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.player.InfDurabilityModule;
import org.phobos.module.player.InfDurabilityModule;

public final class InfDurabilityAttackListener extends ModuleListener.Safe<InfDurabilityModule, AttackEvent2> {
   public void handleObject(
      AttackEvent2 attackEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager, InfDurabilityModule.Type.Attack, null);
   }

   public InfDurabilityAttackListener(InfDurabilityModule infDurabilityModule) {
      super(infDurabilityModule, AttackEvent2.class);
   }
}
