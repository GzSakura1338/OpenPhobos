package org.phobos.core.setting;

public class SettingRef<V, S extends Setting<V>> {
   public final S setting;

   public SettingRef(S setting) {
      this.setting = setting;
   }

   public S getSetting() {
      return this.setting;
   }
}
