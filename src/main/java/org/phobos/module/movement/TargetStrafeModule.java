package org.phobos.module.movement;

import net.minecraft.entity.Entity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.TargetStrafeJumpListener;
import org.phobos.event.TargetStrafePlayerTickListener;
import org.phobos.event.TargetStrafePlayerMoveListener;
import org.phobos.module.Module;

public class TargetStrafeModule extends Module {
   public boolean flag;
   public final Setting<Boolean> speed;
   public static Entity entity;
   public final Setting<Boolean> strafe;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 1.0, 0.2, 6.0));
   public boolean flag2;
   public final Setting<Boolean> fly;
   public static float floatValue;
   public static TargetStrafeModule targetStrafeModule;
   public static boolean flag3 = false;
   public final Setting<Boolean> manual;

   @Override
   public void run4() {
      flag3 = false;
   }

   public static float computeFloat(float f) {
      return flag3 && entity != null ? floatValue : f;
   }

   public static TargetStrafeModule getTargetStrafeModule() {
      return targetStrafeModule;
   }

   public TargetStrafeModule(PhobosClient phobosClient) {
      super(phobosClient, "TargetStrafe", Categories.movement, "Strafes around enemies");
      this.speed = this.resolveSetting2(new BooleanSetting("Speed"));
      this.fly = this.resolveSetting2(new BooleanSetting("Fly"));
      this.manual = this.resolveSetting2(new BooleanSetting("Manual"));
      this.strafe = this.resolveSetting2(new BooleanSetting("Strafe"));
      this.handleListener(new TargetStrafePlayerTickListener(this));
      this.handleListener(new TargetStrafeJumpListener(this));
      this.handleListener(new TargetStrafePlayerMoveListener(this));
      targetStrafeModule = this;
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
