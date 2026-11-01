package org.phobos.module.render;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class ZoomModule extends Module {
   public final Setting<Double> amount;
   public final Setting<Boolean> cinematic = this.resolveSetting2(new BooleanSetting("Cinematic", false));

   @Override
   public void run4() {
      this.phobosClient.getAnimationManager().run2();
   }

   @Override
   public void run3() {
      this.phobosClient.getAnimationManager().handleDouble(this.amount.getValue(), this.cinematic.getValue());
   }

   public ZoomModule(PhobosClient phobosClient) {
      super(phobosClient, "Zoom", Categories.render, "Zooms in");
      this.amount = this.resolveSetting2(new NumberSetting<>("Amount", 30.0, 5.0, 90.0));
   }
}
