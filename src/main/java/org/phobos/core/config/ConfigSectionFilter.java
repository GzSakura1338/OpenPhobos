package org.phobos.core.config;

import java.util.HashSet;
import java.util.Set;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.Setting;

public class ConfigSectionFilter {
   public final Set<BINDSMODULESEnum> bINDSMODULESEnumSet = new HashSet<>();

   public boolean testSetting(Setting<?> setting) {
      if (setting instanceof BindSetting) {
         return this.testBINDSMODULESEnum(BINDSMODULESEnum.BINDS);
      } else {
         return setting instanceof ColorSetting ? this.testBINDSMODULESEnum(BINDSMODULESEnum.COLORS) : this.testBINDSMODULESEnum(BINDSMODULESEnum.SETTINGS);
      }
   }

   public boolean check() {
      return !this.testBINDSMODULESEnum(BINDSMODULESEnum.SETTINGS)
         && !this.testBINDSMODULESEnum(BINDSMODULESEnum.BINDS)
         && !this.testBINDSMODULESEnum(BINDSMODULESEnum.VISUALS);
   }

   public void handleB8OeGUh5Wu89mLeMNeBPr1SnBQ2MmivU(BINDSMODULESEnum... bINDSMODULESEnums) {
      this.bINDSMODULESEnumSet.addAll(Set.of(bINDSMODULESEnums));
   }

   public void clear() {
      this.bINDSMODULESEnumSet.clear();
   }

   public boolean testBINDSMODULESEnum(BINDSMODULESEnum bINDSMODULESEnum) {
      return this.bINDSMODULESEnumSet.isEmpty() || this.bINDSMODULESEnumSet.contains(bINDSMODULESEnum);
   }
}
