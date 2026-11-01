package org.phobos.core.setting;

import org.phobos.module.Module;

public class SettingPages<T extends Enum<T>> {
   public final EnumSetting<T> page;

   public SettingPages(Module module, T enumValue) {
      module.resolveSetting2(this.page = new EnumSetting<>("Page", enumValue));
   }

   public void handleEnumValue(T enumValue, Setting<?>... settings) {
      for (Setting<?> setting : settings) {
         setting.resolveSetting3(() -> this.page.getValue().equals(enumValue));
      }
   }

   public void handleEnumValue2(T enumValue, SettingGroup... settingGroups) {
      for (SettingGroup settingGroup : settingGroups) {
         settingGroup.resolveSettingGroup5(() -> this.page.getValue().equals(enumValue));
      }
   }

   public void handleEnumValue3(T enumValue, SettingSubsection... settingSubsections) {
      for (SettingSubsection settingSubsection : settingSubsections) {
         settingSubsection.setVisibility(() -> this.page.getValue().equals(enumValue));
      }
   }
}
