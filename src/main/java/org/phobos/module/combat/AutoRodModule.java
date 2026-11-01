package org.phobos.module.combat;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoRodPlayerTickListener;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;

public class AutoRodModule extends Module {
   public final Setting<Integer> hold;
   public final Setting<Integer> timeoutTicks;
   public final Setting<Integer> step;
   public final Setting<Boolean> timeout;
   public final Setting<Integer> predictionTicks;
   public final Setting<Boolean> disable;
   public float[] floatArray;
   public int intValue;
   public int intValue2;
   public final Setting<Boolean> prediction;
   public final Setting<Double> distance = this.resolveSetting2(new NumberSetting<>("Distance", 4.0, 0.1, 7.0));
   public final Setting<Long> delay;
   public final Setting<Double> range = this.resolveSetting2(new NumberSetting<>("Range", 5.0, 0.3, 10.0));
   public AutoRodModule.Stage stage;

   @Override
   public void run3() {
      this.stage = AutoRodModule.Stage.None;
      this.delay.run2();
   }

   public AutoRodModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoRod", Categories.combat, "Hooks closest enemy with a rod");
      this.step = this.resolveSetting2(new NumberSetting<>("Step", 0, 0, 5, "t"));
      this.hold = this.resolveSetting2(new NumberSetting<>("Hold", 2, 1, 5, "t"));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 0L, 0L, 2000L, true));
      this.prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
      this.predictionTicks = this.resolveSetting2(new NumberSetting<>("PredictionTicks", 2, 1, 4));
      this.timeout = this.resolveSetting2(new BooleanSetting("Timeout", true));
      this.timeoutTicks = this.resolveSetting2(new NumberSetting<>("TimeoutTicks", 6, 1, 32, "t"));
      this.disable = this.resolveSetting2(new BooleanSetting("Disable").resolveSetting7("Disable after execution"));
      this.stage = AutoRodModule.Stage.None;
      this.intValue2 = 0;
      this.intValue = 0;
      this.resolveSettingGroup(this.prediction, this.predictionTicks);
      this.resolveSettingGroup(this.timeout, this.timeoutTicks);
      this.handleListener(new AutoRodPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   @Override
   public String nothing() {
      return this.disable.getValue() ? "Macro" : "Auto";
   }

   @Override
   public void run4() {
      SilentHotbarModule.getSilentHotbarManager().handleObject2(this);
      this.stage = AutoRodModule.Stage.None;
   }

   public enum Stage {
      None,
      Aim,
      Throw;
   }
}
