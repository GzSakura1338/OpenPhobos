package org.phobos.module.movement;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.module.Module;

public class FireworksModule extends Module {
   public static FireworksModule fireworksModule;
   public final Setting<Double> boost = this.resolveSetting2(new NumberSetting<>("Boost", 100.0, 100.0, 300.0, "%"));

   public FireworksModule(PhobosClient phobosClient) {
      super(phobosClient, "Fireworks", Categories.movement, "Controls fireworks");
      fireworksModule = this;
   }

   public static double computeDouble(double d) {
      return fireworksModule == null ? d : d * (fireworksModule.boost.getValue() / 100.0);
   }

   @Override
   public String nothing() {
      return MathUtils.computeDouble(this.boost.getValue(), 2) + "%";
   }

   public static boolean isFlag() {
      return fireworksModule != null && fireworksModule.check7();
   }
}
