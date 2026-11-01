package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.slot.Slot;
import org.phobos.api.KeyValue;
import org.phobos.core.event.events.StartBreakEvent;
import org.phobos.module.player.AutoToolModule;
import org.phobos.module.player.SilentHotbarModule;

public final class AutoToolStartBreakListener extends ModuleListener.Safe<AutoToolModule, StartBreakEvent> {
   public AutoToolStartBreakListener(AutoToolModule autoToolModule) {
      super(autoToolModule, StartBreakEvent.class);
   }

   public void handleObject(
      StartBreakEvent startBreakEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.silent.getValue()) {
         KeyValue<Slot, Float> keyValue = this.module
            .resolveKeyValue(startBreakEvent.getBlockPos(), clientWorld.getBlockState(startBreakEvent.getBlockPos()), clientWorld, clientPlayerEntity);
         if (keyValue.getObject() != null) {
            SilentHotbarModule.getSilentHotbarManager().handleObject3(this.module, keyValue.getObject(), 4);
         }
      }
   }
}
