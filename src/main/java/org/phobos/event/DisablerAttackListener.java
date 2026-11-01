package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;

public final class DisablerAttackListener extends ModuleListener.Safe<DisablerModule, AttackEvent2> {
   public DisablerAttackListener(DisablerModule disablerModule) {
      super(disablerModule, AttackEvent2.class);
   }

   public void handleObject(
      AttackEvent2 attackEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((DisablerModule.Mode)this.module.mode.getValue()) {
         case Sentinel:
            Entity entity = attackEvent2.getEntity();
            if (!clientPlayerEntity.equals(entity)
               && (clientPlayerEntity.distanceTo(entity) >= 3.0F || this.module.intValue >= 3 && (this.module.intValue >= 328 || this.module.intValue <= 5))) {
               attackEvent2.setCancelled(true);
            }
            break;
         case Verus:
            this.module.flag10 = true;
      }
   }
}
