package org.phobos.module.player;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class ReachModule extends Module {
   public static ReachModule reachModule;
   public final Setting<Double> distance = this.resolveSetting2(new NumberSetting<>("Distance", 6.0, 0.1, 6.0));

   public static ReachModule getReachModule() {
      return reachModule;
   }

   public ReachModule(PhobosClient phobosClient) {
      super(phobosClient, "Reach", Categories.player, "Allows you to reach farther away");
      reachModule = this;
   }

   public static double computeDouble(double d) {
      return reachModule.check7() ? reachModule.distance.getValue() : d;
   }
}
