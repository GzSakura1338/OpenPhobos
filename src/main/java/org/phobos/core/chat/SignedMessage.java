package org.phobos.core.chat;

import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;

public class SignedMessage {
   public final Text text;
   public final MessageSignatureData messageSignatureData;

   public Text getText() {
      return this.text;
   }

   public SignedMessage(Text text, MessageSignatureData messageSignatureData) {
      this.text = text;
      this.messageSignatureData = messageSignatureData;
   }

   public MessageSignatureData getMessageSignatureData() {
      return this.messageSignatureData;
   }
}
