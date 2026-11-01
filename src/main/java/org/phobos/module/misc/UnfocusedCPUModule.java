package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.UnfocusedCPUTickListener;
import org.phobos.module.Module;

public class UnfocusedCPUModule extends Module {
   public final Setting<Integer> fPS = this.resolveSetting2(new NumberSetting<>("FPS", 30, 15, 60));

   public UnfocusedCPUModule(PhobosClient phobosClient) {
      super(phobosClient, "UnfocusedCPU", Categories.misc, "Locks your FPS when the game isn't focused");
      this.handleListener(new UnfocusedCPUTickListener(this));
   }
}
