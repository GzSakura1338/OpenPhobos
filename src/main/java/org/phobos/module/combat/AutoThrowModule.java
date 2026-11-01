package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoThrowPlayerTickListener;
import org.phobos.module.Module;

public class AutoThrowModule extends Module {
   public final Setting<Integer> predictionTicks;
   public PlayerEntity playerEntity;
   public final Setting<Boolean> combo;
   public final Setting<AutoThrowModule.Target> target;
   public final TimeGate.Normal normal;
   public final Setting<Integer> step;
   public AutoThrowModule.Stage stage;
   public final Setting<Double> distance;
   public final Setting<Double> threshold;
   public final Setting<Boolean> disable;
   public final Setting<Long> delay;
   public int intValue;
   public final Setting<Boolean> prediction = this.resolveSetting2(new BooleanSetting("Prediction"));
   public final Setting<Integer> balance;
   public int intValue2;
   public float[] floatArray;

   public AutoThrowModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoThrow", Categories.combat, "Throws eggs/snowballs at a target");
      this.predictionTicks = this.resolveSetting2(new NumberSetting<>("PredictionTicks", 2, 1, 4));
      this.target = this.resolveSetting2(new EnumSetting<>("Target", AutoThrowModule.Target.Auto));
      this.balance = this.resolveSetting2(new NumberSetting<>("Balance", 0, 0, 3, "t"));
      this.delay = this.resolveSetting2(new LongNumberSetting("Delay", 450L, 0L, 3000L, true));
      this.disable = this.resolveSetting2(new BooleanSetting("Disable"));
      this.combo = this.resolveSetting2(new BooleanSetting("Combo").resolveSetting7("Terrifying tactics"));
      this.distance = this.resolveSetting2(new NumberSetting<>("Distance", 4.3, 2.0, 7.0));
      this.threshold = this.resolveSetting2(new NumberSetting<>("Threshold", 0.3, 0.01, 0.4));
      this.step = this.resolveSetting2(new NumberSetting<>("Step", 6, 0, 16, "t"));
      this.normal = new TimeGate.Normal();
      this.intValue2 = 0;
      this.intValue = 0;
      this.resolveSettingGroup(this.combo, this.distance, this.threshold, this.step);
      this.resolveSettingGroup(this.prediction, this.predictionTicks);
      this.handleListener(new AutoThrowPlayerTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }

   @Override
   public void run3() {
      this.stage = AutoThrowModule.Stage.None;
      this.intValue = 0;
   }

   @Override
   public String nothing() {
      return this.playerEntity == null ? String.valueOf(this.target.getValue()) : this.playerEntity.getNameForScoreboard();
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, PlayerEntity playerEntity) {
      if (this.combo.getValue() && this.intValue++ >= this.step.getValue()) {
         double d = playerEntity.getBoundingBox().squaredMagnitude(clientPlayerEntity.getClientCameraPosVec(TimerManager.getFloatValue()));
         double d2 = Math.abs(this.distance.getValue() - d);
         return d2 <= this.threshold.getValue();
      } else {
         return false;
      }
   }

   public enum Stage {
      None,
      Aim,
      Throw;
   }

   public enum Target {
      Auto,
      Sync;
   }
}
