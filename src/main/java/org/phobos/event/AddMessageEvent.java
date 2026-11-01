package org.phobos.event;

import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.phobos.core.event.events.CancellableEvent;

public class AddMessageEvent extends CancellableEvent {
   public final MessageSignatureData messageSignatureData;
   public boolean flag = false;
   public Text text;

   public AddMessageEvent(Text text, MessageSignatureData messageSignatureData) {
      this.text = text;
      this.messageSignatureData = messageSignatureData;
   }

   public Text getText() {
      return this.text;
   }

   public MessageSignatureData getMessageSignatureData() {
      return this.messageSignatureData;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void handleText(Text text) {
      this.text = text;
      this.flag = true;
   }
}
