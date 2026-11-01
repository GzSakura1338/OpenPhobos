package org.phobos.module.client;

import com.google.gson.JsonElement;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.command.ChatPrefix;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class PhobosModule extends Module {
   public static PhobosModule phobosModule;
   public final Setting<String> prefix = this.resolveSetting2(new StringSetting("Prefix", "+"));
   public final Setting<Boolean> nCS = this.resolveSetting2(new BooleanSetting("NCS").resolveSetting7("No chat suggestions"));

   public Setting<Boolean> getNCS() {
      return this.nCS;
   }

   public Setting<String> getTextSetting() {
      return this.prefix;
   }

   public static PhobosModule getPhobosModule() {
      return phobosModule;
   }

   public PhobosModule(PhobosClient phobosClient) {
      super(phobosClient, "Phobos", Categories.client);
      this.prefix.handleConsumer2(text -> {
         if (text.length() != 1) {
            ChatPrefix.getINSTANCE().setText("+");
         } else {
            ChatPrefix.getINSTANCE().setText(text);
         }
      });
      phobosModule = this;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.prefix.setFlag4(true);
      super.handleJsonElement(jsonElement);
   }
}
