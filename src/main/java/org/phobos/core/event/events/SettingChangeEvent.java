package org.phobos.core.event.events;

import org.jetbrains.annotations.Nullable;
import org.phobos.api.SettingContainer;
import org.phobos.core.setting.Setting;

public class SettingChangeEvent<T, S extends Setting<T>> extends CancellableEvent {
   @Nullable
   public final SettingContainer settingContainer;
   public final S setting;
   public final T value;

   public SettingChangeEvent(@Nullable SettingContainer settingContainer, S setting, T object) {
      this.settingContainer = settingContainer;
      this.setting = setting;
      this.value = object;
   }

   @Nullable
   public SettingContainer getSettingContainer() {
      return this.settingContainer;
   }

   public T getValue() {
      return this.value;
   }

   public S getSetting() {
      return this.setting;
   }
}
