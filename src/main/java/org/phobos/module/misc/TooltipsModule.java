package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.TooltipsTooltipContextListener;
import org.phobos.module.Module;

public class TooltipsModule extends Module {
   public final Setting<Boolean> icon = this.resolveSetting2(new BooleanSetting("Icon"));
   public static TooltipsModule tooltipsModule;

   public static boolean check() {
      return tooltipsModule != null && tooltipsModule.check7() && tooltipsModule.icon.getValue();
   }

   public TooltipsModule(PhobosClient phobosClient) {
      super(phobosClient, "Tooltips", Categories.misc, "Shows advanced tooltips");
      this.handleListener(new TooltipsTooltipContextListener(this));
      tooltipsModule = this;
   }
}
