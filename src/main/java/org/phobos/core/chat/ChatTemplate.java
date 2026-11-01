package org.phobos.core.chat;

import java.util.function.Consumer;
import java.util.function.Function;

public class ChatTemplate {
   public int intValue = 0;
   public final Function<TextPair, String>[] functionArray;

   public boolean testTextPair(TextPair textPair, Consumer<String> consumer) {
      if (this.functionArray.length == 1) {
         consumer.accept(this.functionArray[0].apply(textPair));
         return true;
      }

      if (this.intValue >= this.functionArray.length) {
         return true;
      }

      consumer.accept(this.functionArray[this.intValue++].apply(textPair));
      return this.intValue >= this.functionArray.length;
   }

   public void run() {
      this.intValue = 0;
   }

   @SafeVarargs
   public ChatTemplate(Function<TextPair, String>... functions) {
      this.functionArray = functions;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public Function<TextPair, String>[] getFunctionArray() {
      return this.functionArray;
   }
}
