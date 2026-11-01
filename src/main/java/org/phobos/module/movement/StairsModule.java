package org.phobos.module.movement;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.StairsPlayerTickListener;
import org.phobos.module.Module;

public class StairsModule extends Module {
   public final Setting<Long> delay = new LongNumberSetting("Delay", 100L, 0L, 1000L, true).resolveSetting6(this);
   public double doubleValue;
   public final TimeGate.Normal normal = new TimeGate.Normal();
   public double doubleValue2;

   public StairsModule(PhobosClient phobosClient) {
      super(phobosClient, "Stairs", Categories.movement, "Makes you really fast on stairs");
      this.handleListener(new StairsPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
