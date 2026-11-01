package org.phobos.event;

import org.phobos.core.event.events.CancellableEvent;

public class SendChatMessageEvent extends CancellableEvent {
   public String message;

   public SendChatMessageEvent(String text) {
      this.message = text;
   }

   public void setMessage(String text) {
      this.message = text;
   }

   public String getMessage() {
      return this.message;
   }
}
