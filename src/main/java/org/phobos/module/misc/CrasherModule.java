package org.phobos.module.misc;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.CrasherTickListener;
import org.phobos.module.Module;

public class CrasherModule extends Module {
   public final Setting<Long> delay;
   public final Setting<CrasherModule.Mode> mode = new EnumSetting<>("Mode", CrasherModule.Mode.Window).resolveSetting6(this);
   public final TimeGate.Normal normal;
   public final Setting<Integer> packets;
   public int intValue;

   public CrasherModule(PhobosClient phobosClient) {
      super(phobosClient, "Crasher", Categories.misc, "Attempts to crash the server");
      this.delay = new LongNumberSetting("Delay", 0L, 0L, 10000L, true).resolveSetting6(this);
      this.packets = new NumberSetting<>("Packets", 6, 1, 10).resolveSetting6(this);
      this.normal = new TimeGate.Normal();
      this.handleListener(new CrasherTickListener(this));
   }

   @Override
   public void run3() {
      this.intValue = 0;
   }

   public enum Mode {
      Window,
      Completion,
      Payload;
   }
}
