package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.event.HighJumpJumpListener;
import org.phobos.event.HighJumpPlayerTickListener;
import org.phobos.module.Module;
import org.phobos.module.render.FreeLookModule;

public class HighJumpModule extends Module {
   public int intValue;
   public boolean flag;
   public final Setting<Float> random;
   public final Setting<Float> factor;
   public final Setting<Integer> ticks;
   public final Setting<Float> slow;
   public final Setting<Boolean> automatic;
   public final Setting<Integer> lag;
   public final Setting<Float> modifier;
   public final Setting<Double> horizontal;
   public final Setting<Float> height;
   public final Setting<HighJumpModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", HighJumpModule.Mode.Polar));
   public int intValue2;
   public HighJumpModule.Stage stage;
   public final Setting<Double> vertical;

   @Override
   public void run4() {
      if (this.flag) {
         this.phobosClient.getTimerManager().run2();
         this.flag = false;
      }
   }

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public HighJumpModule(PhobosClient phobosClient) {
      super(phobosClient, "HighJump", Categories.movement, "Makes you jump higher");
      this.height = this.resolveSetting2(
         new NumberSetting<>("Height", 0.42F, 0.01F, 1.0F).getNumberSetting().resolveSetting4(this.mode, HighJumpModule.Mode.Static)
      );
      this.modifier = this.resolveSetting2(new NumberSetting<>("Modifier", 1.0F, 0.01F, 3.0F).resolveSetting4(this.mode, HighJumpModule.Mode.Factor));
      this.horizontal = this.resolveSetting2(new NumberSetting<>("Horizontal", 1.0, 0.1, 1.0).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.vertical = this.resolveSetting2(new NumberSetting<>("Vertical", 1.0, 0.1, 1.0).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 1, 1, 5).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.lag = this.resolveSetting2(new NumberSetting<>("Lag", 2, 1, 8).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.slow = this.resolveSetting2(new NumberSetting<>("Slow", 0.5F, 0.1F, 0.5F).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.factor = this.resolveSetting2(new NumberSetting<>("Factor", 60.0F, 0.0F, 100.0F, "%").resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.random = this.resolveSetting2(new NumberSetting<>("Random", 10.0F, 0.0F, 45.0F).resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.automatic = this.resolveSetting2(new BooleanSetting("Automatic").resolveSetting4(this.mode, HighJumpModule.Mode.Polar));
      this.flag = false;
      this.intValue = 0;
      this.intValue2 = 0;
      this.handleListener(new HighJumpJumpListener(this));
      this.handleListener(new HighJumpPlayerTickListener(this));
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      float f = FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity)
         + (this.random.testObject3(0.0F) ? 0.0F : MathUtils.computeFloat8(-this.random.getValue(), this.random.getValue()));
      float f2 = -(90.0F * (this.factor.getValue() / 100.0F));
      double d = Math.sin(Math.toRadians(f + 90.0F));
      double d2 = Math.cos(Math.toRadians(f + 90.0F));
      double d3 = 1.0 * this.horizontal.getValue() * d2;
      double d4 = -Math.sin(Math.toRadians(f2)) * this.vertical.getValue();
      double d5 = 1.0 * this.horizontal.getValue() * d;
      clientPlayerEntity.setVelocity(d3, d4, d5);
   }

   @Override
   public void run3() {
      if (this.mode.testObject2(HighJumpModule.Mode.Polar)) {
         this.phobosClient.getTimerManager().handleFloat(this.slow.getValue(), TimerManager.TimerPriority.High);
      }

      this.stage = HighJumpModule.Stage.None;
      this.flag = true;
      this.intValue = 0;
      this.intValue2 = 0;
   }

   public enum Mode {
      Polar,
      Factor,
      Static;
   }

   public enum Stage {
      None,
      Lag,
      Boost;
   }
}
