package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.chat.StringPair2;
import org.phobos.module.misc.AutoGGModule;

public final class AutoGGStringPairListener extends ModuleListener.Safe<AutoGGModule, StringPair2> {
   public AutoGGStringPairListener(AutoGGModule autoGGModule) {
      super(autoGGModule, StringPair2.class);
   }

   public void handleObject(
      StringPair2 stringPair2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.textMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
      if (this.module.chain.getValue()) {
         String text = stringPair2.getText2();
         if (this.module.textMap.containsKey(text)) {
            this.module.handleText3(text, stringPair2.getText());
         }
      }
   }
}
