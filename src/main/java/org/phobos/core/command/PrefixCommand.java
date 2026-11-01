package org.phobos.core.command;

import org.phobos.module.client.PhobosModule;
import org.phobos.core.PhobosClient;

public class PrefixCommand extends Command {
   public PrefixCommand(PhobosClient phobosClient) {
      super(phobosClient, "prefix", "type");
   }

   @Override
   public void handleString5(String[] texts) {
      String text = texts[0];
      if (text.length() != 1) {
         this.handleText2("Invalid prefix length, must be exactly 1");
      } else {
         PhobosModule.getPhobosModule().getTextSetting().setValue(text);
         this.handleText8("Set prefix to %s", text);
      }
   }
}
