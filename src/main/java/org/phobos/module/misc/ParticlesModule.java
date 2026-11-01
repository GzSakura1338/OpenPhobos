package org.phobos.module.misc;

import java.awt.Color;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.ParticlesAttackListener;
import org.phobos.module.Module;

public class ParticlesModule extends Module {
   public final Setting<Integer> multiplier;
   public final Setting<Integer> times;
   public final Setting<Color> color;
   public final Setting<Boolean> square;
   public final Setting<ParticlesModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", ParticlesModule.Mode.Always));
   public final Setting<Double> scale;
   public final Setting<Long> time;
   public static ParticlesModule particlesModule;

   public static ParticlesModule getParticlesModule() {
      return particlesModule;
   }

   public ParticlesModule(PhobosClient phobosClient) {
      super(phobosClient, "Particles", Categories.misc, "Alters hit particles");
      this.multiplier = this.resolveSetting2(
         new NumberSetting<>("Multiplier", 2, 0, 6).resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Multiply))
      );
      this.times = this.resolveSetting2(new NumberSetting<>("Times", 1, 1, 6).resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Always)));
      this.color = this.resolveSetting2(
         new ColorSetting("Color", Color.WHITE).getColorSetting().resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Custom))
      );
      this.scale = this.resolveSetting2(
         new NumberSetting<>("Scale", 0.04, 0.01, 1.0).resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Custom))
      );
      this.square = this.resolveSetting2(new BooleanSetting("Square", false).resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Custom)));
      this.time = this.resolveSetting2(
         new LongNumberSetting("Time", 250L, 0L, 2000L, true).resolveSetting3(this.resolveVisibility2(this.mode, ParticlesModule.Mode.Custom))
      );
      this.handleListener(new ParticlesAttackListener(this));
      particlesModule = this;
   }

   public static int value() {
      return particlesModule.check7() && particlesModule.mode.getValue().equals(ParticlesModule.Mode.Multiply) ? particlesModule.multiplier.getValue() : 1;
   }

   public enum Mode {
      Always,
      Multiply,
      Custom;
   }
}
