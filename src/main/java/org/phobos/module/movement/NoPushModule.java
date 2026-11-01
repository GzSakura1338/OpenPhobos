package org.phobos.module.movement;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class NoPushModule extends Module {
   public static NoPushModule noPushModule;
   public final Setting<Boolean> liquids = this.resolveSetting2(new BooleanSetting("Liquids"));

   public static boolean check2() {
      return noPushModule.check7();
   }

   public NoPushModule(PhobosClient phobosClient) {
      super(phobosClient, "NoPush", Categories.movement, "Prevents you from being pushed by entities and blocks");
      noPushModule = this;
   }

   public static boolean check() {
      return noPushModule.check7() && noPushModule.liquids.getValue();
   }
}
