package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoInteractPlayerTickListener;
import org.phobos.type.SingleMultiEnum;
import org.phobos.module.Module;

public class AutoInteractModule extends Module {
   public final Setting<Integer> delay;
   public final Setting<Integer> iterations;
   public int intValue;
   public final Setting<Double> range;
   public final Setting<Boolean> swing;
   public final Setting<SingleMultiEnum> mode = this.resolveSetting2(new EnumSetting<>("Mode", SingleMultiEnum.Single));

   public AutoInteractModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoInteract", Categories.misc, "Clicks on buttons & levers");
      this.iterations = this.resolveSetting2(new NumberSetting<>("Iterations", 1, 1, 10));
      this.delay = this.resolveSetting2(new NumberSetting<>("Delay", 0, 0, 10));
      this.range = this.resolveSetting2(new NumberSetting<>("Range", 6.0, 0.1, 6.0));
      this.swing = this.resolveSetting2(new BooleanSetting("Swing"));
      this.intValue = 0;
      this.handleListener(new AutoInteractPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   @Override
   public void run3() {
      this.intValue = 0;
   }
}
