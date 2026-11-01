package org.phobos.core.command;

import org.phobos.core.PhobosClient;

public abstract class SyntaxCommand extends Command {
   public SyntaxCommand(PhobosClient phobosClient, String text) {
      super(phobosClient, text);
   }

   public abstract void handleString(String[] var1, String var2);

   @Override
   public boolean testString2(String[] texts, String text) {
      if (!this.testString(texts)) {
         return false;
      }

      this.handleString(texts, text);
      return true;
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= this.getTextArray().length;
   }

   public SyntaxCommand(PhobosClient phobosClient, String text, String... texts) {
      super(phobosClient, text, texts);
   }

   @Override
   public void handleString5(String[] texts) {
   }
}
