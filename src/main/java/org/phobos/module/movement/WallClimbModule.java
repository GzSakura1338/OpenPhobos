package org.phobos.module.movement;

import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.WallClimbTick2Listener;
import org.phobos.event.WallClimbPlayerTickListener;
import org.phobos.event.WallClimbComputeNextListener;
import org.phobos.event.WallClimbReceiveListener;
import org.phobos.module.Module;

public class WallClimbModule extends Module {
   public final Setting<Float> back;
   public final Setting<WallClimbModule.Boost> boost;
   public final Setting<Boolean> packet;
   public final Setting<Float> height;
   public final Setting<Float> threshold;
   public final Setting<Boolean> rotate;
   public final Setting<Long> sequence;
   public final Setting<WallClimbModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", WallClimbModule.Mode.Polar));
   public final TimeGate.Normal normal;
   public final Setting<Long> timeout;
   public boolean flag;
   public final TimeGate.Normal normal2;
   public final Setting<Boolean> reset;

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public boolean check() {
      return !this.normal2.testNumber(this.timeout.getValue());
   }

   public void run() {
      this.normal2.run();
   }

   public WallClimbModule(PhobosClient phobosClient) {
      super(phobosClient, "WallClimb", Categories.movement);
      this.back = this.resolveSetting2(new NumberSetting<>("Back", 0.0F, 0.0F, 1.0F));
      this.boost = this.resolveSetting2(new EnumSetting<>("Boost", WallClimbModule.Boost.Threshold));
      this.height = this.resolveSetting2(new NumberSetting<>("Height", 0.0F, 0.0F, 0.5F).resolveSetting4(this.mode, WallClimbModule.Mode.Polar));
      this.threshold = this.resolveSetting2(
         new NumberSetting<>("Threshold", 0.15F, 0.01F, 0.5F)
            .resolveSetting4(this.mode, WallClimbModule.Mode.Polar)
            .resolveSetting4(this.boost, WallClimbModule.Boost.Threshold)
      );
      this.reset = this.resolveSetting2(new BooleanSetting("Reset"));
      this.sequence = this.resolveSetting2(new LongNumberSetting("Sequence", 150L, 50L, 500L, true));
      this.timeout = this.resolveSetting2(new LongNumberSetting("Timeout", 250L, 0L, 500L, true));
      this.rotate = this.resolveSetting2(new BooleanSetting("Rotate"));
      this.packet = this.resolveSetting2(new BooleanSetting("Packet"));
      this.normal2 = new TimeGate.Normal();
      this.normal = new TimeGate.Normal();
      this.handleListener(new WallClimbComputeNextListener(this));
      this.handleListener(new WallClimbTick2Listener(this));
      this.handleListener(new WallClimbReceiveListener(this));
      this.handleListener(new WallClimbPlayerTickListener(this));
   }

   public enum Boost {
      Threshold,
      Collide;
   }

   public enum Mode {
      Polar;
   }
}
