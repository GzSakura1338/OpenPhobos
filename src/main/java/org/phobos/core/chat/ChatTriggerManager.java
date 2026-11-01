package org.phobos.core.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.phobos.event.Listener;
import org.phobos.core.Feature;
import org.phobos.core.net.packet.MessagePacket239;
import org.phobos.core.net.packet.MessagePacket272;
import org.phobos.core.PhobosClient;
import org.phobos.core.tick.TickCounter;

public class ChatTriggerManager extends Feature {
   public final Map<ChatTrigger, Long> chatTriggerMap = new ConcurrentHashMap<>();
   public final PhobosClient phobosClient;

   public ChatTriggerManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      phobosClient.getPacketBus().handleClassValue(MessagePacket239.class, messagePacket239 -> {
         if (!this.chatTriggerMap.isEmpty()) {
            String text = messagePacket239.getText();

            for (ChatTrigger chatTrigger : this.chatTriggerMap.keySet()) {
               if (chatTrigger.getText2().equals(text)) {
                  chatTrigger.getMessageHandler().handleText(messagePacket239.getMessage());
                  this.chatTriggerMap.remove(chatTrigger);
                  return;
               }
            }
         }
      });
      this.handleListener(new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            ChatTriggerManager.this.chatTriggerMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
         }
      });
   }

   public void handleChatTrigger(ChatTrigger chatTrigger, long l) {
      this.chatTriggerMap.put(chatTrigger, System.currentTimeMillis() + l);
      this.phobosClient.getPacketBus().handleNetworkPacket2(new MessagePacket272(chatTrigger.getText2(), chatTrigger.getText(), chatTrigger.getMessage()));
   }
}
