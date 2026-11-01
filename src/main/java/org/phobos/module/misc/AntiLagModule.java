package org.phobos.module.misc;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AntiLagTickListener;
import org.phobos.event.AntiLagReceiveEntitySpawnListener;
import org.phobos.module.Module;

public class AntiLagModule extends Module {
   public final Setting<Boolean> itemFix;
   public final TimeGate.Normal normal;
   public int intValue;
   public final Setting<Boolean> entityFix = new BooleanSetting("EntityFix").resolveSetting6(this);

   @Override
   public void run3() {
      this.intValue = 0;
   }

   public AntiLagModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiLag", Categories.misc, "Prevents FPS drops when server is at fault");
      this.itemFix = new BooleanSetting("ItemFix").resolveSetting6(this);
      this.normal = new TimeGate.Normal();
      this.handleListener(new AntiLagReceiveEntitySpawnListener(this));
      this.handleListener(new AntiLagTickListener(this));
   }
}
