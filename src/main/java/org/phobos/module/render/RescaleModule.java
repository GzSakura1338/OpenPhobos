package org.phobos.module.render;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class RescaleModule extends Module {
   public final Setting<Float> ratio = this.resolveSetting2(new NumberSetting<>("Ratio", 1.0F, 0.1F, 3.0F));
   public static RescaleModule rescaleModule;

   public static float value() {
      return rescaleModule.check7() ? rescaleModule.ratio.getValue() : (float)mc.getWindow().getFramebufferWidth() / mc.getWindow().getFramebufferHeight();
   }

   public RescaleModule(PhobosClient phobosClient) {
      super(phobosClient, "Rescale", Categories.render, "Changes aspect ratio");
      rescaleModule = this;
   }
}
