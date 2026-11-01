package org.phobos.module.misc;

import java.util.Map;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import org.phobos.api.Subscribe;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.PlaySoundEvent;
import org.phobos.module.Module;

public class AntiSoundModule extends Module {
   public final Setting<Map<SoundEvent, Boolean>> sounds = this.resolveSetting2(
      new MultiSelectSetting<>("Sounds", SoundEvent.class).resolveMultiSelectSetting5(multiSelectSetting -> {
         for (SoundEvent soundEvent : Registries.SOUND_EVENT) {
            multiSelectSetting.resolveMultiSelectSetting(soundEvent, I18n.translate(soundEvent.getId().toString(), new Object[0]));
         }
      })
   );

   @Subscribe
   public void handlePlaySoundEvent(PlaySoundEvent playSoundEvent) {
      if (this.sounds.getValue().get(playSoundEvent.getSoundEvent())) {
         playSoundEvent.setCancelled(true);
      }
   }

   public AntiSoundModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiSound", Categories.misc);
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
