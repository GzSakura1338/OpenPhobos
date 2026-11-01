package org.phobos.module.movement;

import net.minecraft.util.math.Vec3d;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.HoleShiftPlayerMoveListener;
import org.phobos.module.Module;

public class HoleShiftModule extends Module {
   public final Setting<Float> height;
   public final Setting<Boolean> step;
   public final Setting<Integer> ticks = new NumberSetting<>("Ticks", 5, 1, 20).resolveSetting6(this);
   public final Setting<Double> rangeValue;
   public final Setting<Boolean> normal;
   public final Setting<Boolean> range = new BooleanSetting("Range").resolveSetting6(this);
   public Vec3d vec3d;

   @Override
   public void run3() {
      if (this.step.getValue()) {
         this.phobosClient.getMotionManager().setFlag4(true);
         this.phobosClient.getMotionManager().setFlag(this.normal.getValue());
         this.phobosClient.getMotionManager().setFloatValue(this.height.getValue());
      }
   }

   @Override
   public void run4() {
      if (this.step.getValue()) {
         this.phobosClient.getMotionManager().setFlag4(false);
      }
   }

   public HoleShiftModule(PhobosClient phobosClient) {
      super(phobosClient, "HoleShift", Categories.movement, "Simulates lag after leaving a hole");
      this.rangeValue = new NumberSetting<>("RangeValue", 6.0, 0.1, 30.0).resolveSetting6(this);
      this.step = this.resolveSetting2(new BooleanSetting("Step"));
      this.normal = this.resolveSetting2(new BooleanSetting("Normal"));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 2.0F, 0.6F, 2.0F));
      this.resolveSettingGroup(this.range, this.rangeValue);
      this.resolveSettingGroup(this.step, this.normal, this.height);
      this.handleListener(new HoleShiftPlayerMoveListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   public enum State {
      None,
      Timer,
      Lag;
   }
}
