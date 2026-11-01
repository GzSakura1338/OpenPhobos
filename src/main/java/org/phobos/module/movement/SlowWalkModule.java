package org.phobos.module.movement;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.SlowWalkPlayerTickListener;
import org.phobos.module.Module;

public class SlowWalkModule extends Module {
   public final Setting<Float> acceleration = this.resolveSetting2(new NumberSetting<>("Acceleration", 1.4F, 1.1F, 2.0F));
   public final Setting<Float> shift = this.resolveSetting2(new NumberSetting<>("Shift", 0.7F, 0.1F, 0.9F));
   public boolean flag;
   public final TimeGate.Normal normal;
   public final Setting<Long> balance = this.resolveSetting2(new LongNumberSetting("Balance", 250L, 0L, 500L, true));

   public SlowWalkModule(PhobosClient phobosClient) {
      super(phobosClient, "SlowWalk", Categories.movement, "Balances timer speed");
      this.normal = new TimeGate.Normal();
      this.flag = false;
      this.handleListener(new SlowWalkPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   @Override
   public void run4() {
      this.phobosClient.getTimerManager().run2();
   }

   @Override
   public void run3() {
      this.flag = false;
   }
}
