package org.phobos.module.movement;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.BoatFlyPlayerMoveListener;
import org.phobos.module.Module;

public class BoatFlyModule extends Module {
   public final Setting<BoatFlyModule.Mode> mode = new EnumSetting<>("Mode", BoatFlyModule.Mode.Grim).resolveSetting6(this);
   public boolean flag = false;

   @Override
   public void run3() {
      this.flag = false;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public BoatFlyModule(PhobosClient phobosClient) {
      super(phobosClient, "BoatFly", Categories.movement, "Allows you to fly inside of a boat");
      this.handleListener(new BoatFlyPlayerMoveListener(this));
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   public enum Mode {
      Grim;
   }
}
