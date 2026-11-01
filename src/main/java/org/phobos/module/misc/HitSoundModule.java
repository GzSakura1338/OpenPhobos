package org.phobos.module.misc;

import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosSounds;
import org.phobos.core.PhobosClient;
import org.phobos.module.TargetModule;
import org.phobos.core.module.Categories;
import org.phobos.event.HitSoundAttackListener;
import org.phobos.event.HitSoundPostReceiveEntityDamageListener;

public class HitSoundModule extends TargetModule {
   public final Setting<Boolean> sync;
   public final Setting<Float> volume;
   public final Setting<PhobosSounds.Sound> mode = new EnumSetting<>("Mode", PhobosSounds.Sound.Wood).resolveSetting6(this);

   public HitSoundModule(PhobosClient phobosClient) {
      super(phobosClient, "HitSound", Categories.misc, "Plays a sound when you hit an entity");
      this.sync = new BooleanSetting("Sync").resolveSetting6(this);
      this.volume = new NumberSetting<>("Volume", 1.0F, 0.0F, 1.0F).resolveSetting6(this);
      this.handleListener(new HitSoundAttackListener(this));
      this.handleListener(new HitSoundPostReceiveEntityDamageListener(this));
      this.mode.handleConsumer2(sound -> phobosClient.getPhobosSounds().handleSoundEvent(sound.resolveSoundEvent(phobosClient), this.volume.getValue()));
   }
}
