package org.phobos.core.command;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.PhobosClient;

public abstract class Command implements MinecraftHolder {
   public final String[] textArray;
   public final PhobosClient phobosClient;
   public final String name;

   public String getName() {
      return this.name;
   }

   public boolean testString2(String[] texts, String text) {
      if (!this.testString(texts)) {
         return false;
      }

      this.handleString5(texts);
      return true;
   }

   @Nullable
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      return null;
   }

   public String describe() {
      return this.name + " " + String.join(" ", this.textArray);
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void handleText2(String text) {
      this.phobosClient.getCommandManager().handleText2(text);
   }

   public Command(PhobosClient phobosClient, String text) {
      this(phobosClient, text, new String[0]);
   }

   public void handleText6(String text) {
      this.phobosClient.getCommandManager().handleText6(text);
   }

   public Command(PhobosClient phobosClient, String text, String... texts) {
      this.phobosClient = phobosClient;
      this.name = text;
      this.textArray = texts;
   }

   public void handleText7(Text text) {
      this.phobosClient.getCommandManager().handleText7(text);
   }

   public String[] getTextArray() {
      return this.textArray;
   }

   public abstract void handleString5(String[] var1);

   public void handleText8(String text, Object... objects) {
      this.phobosClient.getCommandManager().handleText6(text.formatted(objects));
   }

   public boolean testString(String[] texts) {
      return this.textArray.length == texts.length;
   }
}
