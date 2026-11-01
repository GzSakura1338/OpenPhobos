package org.phobos.module.movement;

import java.util.Map;
import java.util.function.Predicate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.player.PlayerUtils;
import org.phobos.event.AnchorMoveListener;
import org.phobos.module.Module;

public class AnchorModule extends Module {
   public final Setting<Double> acceleration;
   public final Setting<Boolean> partial;
   public final Setting<Map<AnchorModule.Disable, Boolean>> disablers;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 1.5, 0.5, 2.0));
   public final Setting<Double> drag;

   public AnchorModule(PhobosClient phobosClient) {
      super(phobosClient, "Anchor", Categories.movement);
      this.disablers = this.resolveSetting2(new MultiSelectSetting<>("Disablers", AnchorModule.Disable.class).resolveMultiSelectSetting6(AnchorModule.Disable.values()));
      this.drag = this.resolveSetting2(new NumberSetting<>("Drag", 2.0, 1.0, 3.0));
      this.acceleration = this.resolveSetting2(new NumberSetting<>("Acceleration", 1.0, 0.5, 2.0));
      this.partial = this.resolveSetting2(new BooleanSetting("Partial"));
      this.handleListener(new AnchorMoveListener(this));
   }

   public enum Disable {
      Movement(__ -> PlayerUtils.check4()),
      Step(phobos -> phobos.getMotionManager().isFlag42()),
      Speed(phobos -> SpeedModule.getSpeedModule().check7());

      public final Predicate<PhobosClient> test;

      Disable(final Predicate<PhobosClient> test) {
         this.test = test;
      }

      public Predicate<PhobosClient> getTest() {
         return this.test;
      }
   }
}
