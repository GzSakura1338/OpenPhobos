package org.phobos.module.combat;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.WTapTickListener;
import org.phobos.event.WTapAttackListener;
import org.phobos.module.Module;

public class WTapModule extends Module {
   public final TimeGate.Normal normal;
   public long longValue;
   public long longValue2;
   public boolean flag;
   public final Setting<Double> chance = new NumberSetting<>("Chance", 100.0, 0.0, 100.0).resolveSetting6(this);

   public WTapModule(PhobosClient phobosClient) {
      super(phobosClient, "WTap", Categories.combat, "Performs w-tap technique");
      this.normal = new TimeGate.Normal();
      this.flag = false;
      this.handleListener(new WTapTickListener(this));
      this.handleListener(new WTapAttackListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public void run() {
      if (this.flag) {
         mc.options.forwardKey.setPressed(false);
         this.flag = false;
      }
   }

   @Override
   public String nothing() {
      return this.longValue == 0L ? "1" : (this.longValue2 != 0L ? "2" : "0");
   }
}
