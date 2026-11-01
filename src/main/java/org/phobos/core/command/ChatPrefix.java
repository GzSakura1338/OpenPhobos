package org.phobos.core.command;

public class ChatPrefix {
   public static final ChatPrefix INSTANCE = new ChatPrefix();
   public String text = "+";

   public void setText(String text) {
      this.text = text;
   }

   public static ChatPrefix getINSTANCE() {
      return INSTANCE;
   }

   public String getText() {
      return this.text;
   }

   public boolean testText(String text) {
      return text.startsWith(this.text);
   }
}
