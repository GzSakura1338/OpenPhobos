package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AntiAimPlayerTickListener;
import org.phobos.module.Module;

public class AntiAimModule extends Module {
   public final Setting<Float> pitch;
   public final Setting<AntiAimModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", AntiAimModule.Mode.Static));
   public final Setting<Float> speed;
   public final Setting<Float> angle;
   public final Setting<Float> base = this.resolveSetting2(new NumberSetting<>("Base", 180.0F, -180.0F, 180.0F));
   public float floatValue;
   public final Setting<Long> delay;

   public AntiAimModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiAim", Categories.misc, "Turns your head around");
      this.pitch = this.resolveSetting2(new NumberSetting<>("Pitch", 90.0F, -90.0F, 90.0F));
      this.angle = this.resolveSetting2(
         new NumberSetting<>("Angle", 35.0F, 0.0F, 90.0F).resolveSetting3(() -> this.mode.getValue().equals(AntiAimModule.Mode.Jitter))
      );
      this.delay = this.resolveSetting2(
         new LongNumberSetting("Delay", 3L, 1L, 50L, true).resolveSetting3(() -> this.mode.getValue().equals(AntiAimModule.Mode.Jitter))
      );
      this.speed = this.resolveSetting2(
         new NumberSetting<>("Speed", 1.0F, 0.1F, 100.0F).resolveSetting3(() -> this.mode.getValue().equals(AntiAimModule.Mode.Spin))
      );
      this.floatValue = 0.0F;
      this.resolveSettingGroup2("Values", this.base, this.pitch, this.angle, this.delay, this.speed);
      this.handleListener(new AntiAimPlayerTickListener(this));
   }

   public enum Mode {
      Static,
      Jitter,
      Spin,
      Stare;
   }
}
