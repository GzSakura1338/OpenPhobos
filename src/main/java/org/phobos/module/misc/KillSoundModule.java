package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosSounds;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.KillSoundAttackersRecordListener;
import org.phobos.module.Module;

public class KillSoundModule extends Module {
   public final Setting<PhobosSounds.Sound> mode = new EnumSetting<>("Mode", PhobosSounds.Sound.Normal).resolveSetting6(this);
   public final Setting<Float> volume = new NumberSetting<>("Volume", 1.0F, 0.1F, 1.0F).resolveSetting6(this);

   public KillSoundModule(PhobosClient phobosClient) {
      super(phobosClient, "KillSound", Categories.misc, "Plays a sound when someone dies");
      this.handleListener(new KillSoundAttackersRecordListener(this));
      this.mode.handleConsumer2(sound -> {
         if (!ConfigManager.flag) {
            phobosClient.getPhobosSounds().handleSoundEvent(sound.resolveSoundEvent(phobosClient), this.volume.getValue());
         }
      });
   }
}
