package org.phobos.module.player;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.InvLockSlotClickListener;
import org.phobos.module.Module;

public class InvLockModule extends Module {
   public final Setting<Boolean> armor = this.resolveSetting2(new BooleanSetting("Armor").resolveSetting7("Block all armor slots"));
   public final Setting<Boolean> offhand = this.resolveSetting2(new BooleanSetting("Offhand").resolveSetting7("Block offhand slot"));

   public InvLockModule(PhobosClient phobosClient) {
      super(phobosClient, "InvLock", Categories.player, "Prevents you from accidentally picking up items");
      this.handleListener(new InvLockSlotClickListener(this));
   }
}
