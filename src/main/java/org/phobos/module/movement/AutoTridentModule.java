package org.phobos.module.movement;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoTridentPlayerMoveListener;
import org.phobos.event.AutoTridentOnStoppedUsingListener;
import org.phobos.module.Module;

public class AutoTridentModule extends Module {
   public static AutoTridentModule autoTridentModule;
   public final Setting<AutoTridentModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AutoTridentModule.Mode.Vanilla));
   public final TimeGate.Normal normal = new TimeGate.Normal();

   public static AutoTridentModule getAutoTridentModule() {
      return autoTridentModule;
   }

   public AutoTridentModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTrident", Categories.movement, "Automatically uses trident");
      this.handleListener(new AutoTridentPlayerMoveListener(this));
      this.handleListener(new AutoTridentOnStoppedUsingListener(this));
      autoTridentModule = this;
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public enum Mode {
      Vanilla,
      Grim;
   }
}
