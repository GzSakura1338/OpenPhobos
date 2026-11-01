package org.phobos.core.chat;

import org.phobos.api.MessageHandler;

public class ChatTrigger {
   public final String text;
   public final String text2 = ChatTextUtils.describeInt(16);
   public final MessageHandler messageHandler;
   public final String message;

   public static ChatTrigger createChatTrigger(String text, String text2, MessageHandler messageHandler) {
      return new ChatTrigger(text, text2, messageHandler);
   }

   public MessageHandler getMessageHandler() {
      return this.messageHandler;
   }

   public String getText2() {
      return this.text2;
   }

   public String getMessage() {
      return this.message;
   }

   public String getText() {
      return this.text;
   }

   public ChatTrigger(String text, String text2, MessageHandler messageHandler) {
      this.text = text;
      this.message = text2;
      this.messageHandler = messageHandler;
   }
}
