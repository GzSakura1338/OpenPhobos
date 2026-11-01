package org.phobos.module.player;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AntiHungerSendClientCommandListener;
import org.phobos.event.AntiHungerSendListener;
import org.phobos.module.Module;

public class AntiHungerModule extends Module {
   public final Setting<Boolean> sprint;
   public final Setting<Boolean> ground = new BooleanSetting("Ground", true).resolveSetting6(this);

   public AntiHungerModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiHunger", Categories.player, "Prevents you from losing hunger");
      this.sprint = new BooleanSetting("Sprint", true).resolveSetting6(this);
      this.handleListener(new AntiHungerSendClientCommandListener(this));
      this.handleListener(new AntiHungerSendListener(this));
   }
}
