package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class PortalsModule extends Module {
   public final Setting<Boolean> chat = new BooleanSetting("Chat", true).resolveSetting6(this);
   public static PortalsModule portalsModule;

   public PortalsModule(PhobosClient phobosClient) {
      super(phobosClient, "Portals", Categories.misc, "Portal tweaks");
      portalsModule = this;
   }

   public static boolean check() {
      return !portalsModule.check7() || !portalsModule.chat.getValue();
   }
}
