package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.chat.ChatTrigger;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.math.MathUtils;
import org.phobos.data.AttackersRecord;
import org.phobos.module.misc.AutoGGModule;
import org.phobos.module.misc.AutoGGModule;

public final class AutoGGAttackersRecordListener extends ModuleListener.Safe<AutoGGModule, AttackersRecord> {
   public void handleObject(
      AttackersRecord attackersRecord,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (attackersRecord.testPlayerEntity(clientPlayerEntity)
         && !attackersRecord.check()
         && !this.phobosClient.getFriendList().testUUID(attackersRecord.victim().getUuid())) {
         switch ((AutoGGModule.Mode)this.module.mode.getValue()) {
            case GG:
               this.module.handleText("gg");
               break;
            case LOL:
               this.module.handleText("LOL");
               break;
            case Instagram:
               String[] texts = this.module.values3();
               String text = texts[MathUtils.computeInt5(0, texts.length - 1)];
               if (text.contains("%player%")) {
                  this.module.handleText(text.replace("%player%", attackersRecord.victim().getNameForScoreboard()));
                  return;
               }

               this.module.handleText(text);
               break;
            case Soundcloud:
               String[] texts2 = this.module.values();
               String text2 = texts2[MathUtils.computeInt5(0, texts2.length - 1)];
               if (text2.contains("%player%")) {
                  this.module.handleText(text2.replace("%player%", attackersRecord.victim().getNameForScoreboard()));
                  return;
               }

               this.module.handleText(text2);
               break;
            case Brainrot:
               String[] texts3 = this.module.values2();
               this.module.handleText(texts3[MathUtils.computeInt5(0, texts3.length - 1)]);
               break;
            case AI:
               ChatTrigger chatTrigger = ChatTrigger.createChatTrigger(this.module.describe(), "lel nn who are you", this.module::handleText);
               this.phobosClient.getChatTriggerManager().handleChatTrigger(chatTrigger, 5000L);
               this.module.handleText2(ChatTextUtils.describeText4(attackersRecord.victim().getNameForScoreboard()), this.module.await.getValue());
         }
      }
   }

   public AutoGGAttackersRecordListener(AutoGGModule autoGGModule) {
      super(autoGGModule, AttackersRecord.class);
   }
}
