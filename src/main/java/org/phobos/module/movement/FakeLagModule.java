package org.phobos.module.movement;

import org.phobos.api.TickGate;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.tick.TickManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.FakeLagPlayerTickListener;
import org.phobos.module.Module;

public class FakeLagModule extends Module {
   public final Setting<TickManager.Tick> tick;
   public final TickGate.Normal normal;
   public final Setting<NumberRange.Mutable<Integer>> ticks;
   public final Setting<Integer> delay = this.resolveSetting2(new NumberSetting<>("Delay", 2, 1, 10));

   public FakeLagModule(PhobosClient phobosClient) {
      super(phobosClient, "FakeLag", Categories.movement, "Simulates lag (incoming and outcoming)");
      this.ticks = this.resolveSetting2(new MutableNumberSetting<>("Ticks", 4, 1, 10));
      this.tick = this.resolveSetting2(new EnumSetting<>("Tick", TickManager.Tick.Player));
      this.normal = new TickGate.Normal();
      this.handleListener(new FakeLagPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }
}
