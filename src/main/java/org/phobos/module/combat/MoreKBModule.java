package org.phobos.module.combat;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.MoreKBPlayerMoveListener;
import org.phobos.event.MoreKBAttackListener;
import org.phobos.module.Module;

public class MoreKBModule extends Module {
   public final Setting<Double> chance;
   public boolean flag;
   public final Setting<MoreKBModule.Mode> mode = new EnumSetting<>("Mode", MoreKBModule.Mode.Legit_Fast).resolveSetting6(this);

   public MoreKBModule(PhobosClient phobosClient) {
      super(phobosClient, "MoreKB", Categories.combat, "Exploits sprinting to deal additional knockback");
      this.chance = new NumberSetting<>("Chance", 100.0, 0.0, 100.0, "%").resolveSetting6(this);
      this.handleListener(new MoreKBAttackListener(this));
      this.handleListener(new MoreKBPlayerMoveListener(this));
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue()).replaceAll("_", " ");
   }

   public enum Mode {
      Legit_Fast,
      Packet;
   }
}
