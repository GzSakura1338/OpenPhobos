package org.phobos.module.combat;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.BowSpamPlayerTickListener;
import org.phobos.module.Module;

public class BowSpamModule extends Module {
   public final Setting<Integer> ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 3, 1, 20));

   public BowSpamModule(PhobosClient phobosClient) {
      super(phobosClient, "BowSpam", Categories.combat, "Bow spam");
      this.handleListener(new BowSpamPlayerTickListener(this));
   }
}
