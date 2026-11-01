package org.phobos.event;

import java.util.Random;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.misc.SpammerModule;
import org.phobos.module.misc.SpammerModule;

public final class SpammerTickListener extends ModuleListener.Safe<SpammerModule, TickEvent2> {
   public SpammerTickListener(SpammerModule spammerModule) {
      super(spammerModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.normal.testNumber2(this.module.delay.getValue())) {
         switch ((SpammerModule.Mode)this.module.mode.getValue()) {
            case Text:
               String text = this.module.text.getValue();
               if (!text.isEmpty()) {
                  clientPlayerEntity.networkHandler.sendChatMessage(text + (this.module.antiKick.getValue() ? " " + this.module.describe() : ""));
               }
               break;
            case File:
               if (this.module.textList.isEmpty()) {
                  this.module.handleText6("Spammer file is empty!");
                  return;
               }

               if (this.module.random.getValue()) {
                  Random random = new Random();
                  int i = random.nextInt(this.module.textList.size());
                  String text2 = this.module.textList.get(i) + (this.module.antiKick.getValue() ? " " + this.module.describe() : "");
                  clientPlayerEntity.networkHandler.sendChatMessage(text2);
                  return;
               }

               if (this.module.intValue == this.module.textList.size() - 1) {
                  this.module.intValue = 0;
               }

               String text3 = this.module.textList.get(this.module.intValue) + (this.module.antiKick.getValue() ? " " + this.module.describe() : "");
               clientPlayerEntity.networkHandler.sendChatMessage(text3);
               this.module.intValue++;
         }
      }
   }
}
