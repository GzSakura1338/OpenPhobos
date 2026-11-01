package org.phobos.core.setting;

import org.phobos.api.SettingContainer;
import org.phobos.core.user.UserSession;

public class DebugSettings {
   public final Setting<Boolean> debug = new BooleanSetting("Debug").getSetting2();

   public boolean check() {
      return UserSession.flag3 && this.debug.getValue();
   }

   public static DebugSettings resolveDebugSettings(SettingContainer settingContainer) {
      DebugSettings debugSettings = new DebugSettings();
      settingContainer.resolveSetting2(debugSettings.debug);
      return debugSettings;
   }
}
