package org.phobos.core.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;

public class SettingGroup implements Visibility, TextProvider {
   public final Setting<Boolean> booleanValueSetting;
   @Nullable
   public String text;
   public Visibility visibility;
   public final List<Setting<?>> settingList = new ArrayList<>();
   public final String name;

   @Nullable
   @Override
   public String getText() {
      return this.text;
   }

   public SettingGroup resolveSettingGroup(Setting<Boolean> setting) {
      return this.resolveSettingGroup4(setting, true);
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public SettingGroup(String text, Setting<?>... settings) {
      this.visibility = Visibility.INSTANCE;
      this.name = text;
      this.booleanValueSetting = null;
      Collections.addAll(this.settingList, settings);
   }

   public SettingGroup resolveSettingGroup2(Setting<?> setting) {
      this.settingList.add(setting);
      return this;
   }

   @SafeVarargs
   public final <T> SettingGroup resolveSettingGroup3(Setting<T> setting, T... objects) {
      Visibility visibility = Visibility.resolveVisibility3(setting, objects);
      this.visibility = this.visibility == null ? visibility : this.visibility.resolveVisibility5(visibility);
      return this;
   }

   public <T> SettingGroup resolveSettingGroup4(Setting<T> setting, T object) {
      this.visibility = this.visibility == null
         ? () -> setting.getValue().equals(object)
         : this.visibility.resolveVisibility5(() -> setting.getValue().equals(object));
      return this;
   }

   @Override
   public boolean isFlag8() {
      return this.visibility.isFlag8();
   }

   @Contract(pure = true)
   public SettingGroup(Setting<Boolean> setting, Setting<?>... settings) {
      this.visibility = Visibility.INSTANCE;
      this.name = setting.getName();
      this.booleanValueSetting = setting;
      Collections.addAll(this.settingList, settings);
   }

   public List<Setting<?>> getSettingList() {
      return this.settingList;
   }

   public String getName() {
      return this.name;
   }

   public Visibility getVisibility() {
      return this.visibility;
   }

   public SettingGroup resolveSettingGroup5(Visibility visibility) {
      this.visibility = this.visibility == null ? visibility : this.visibility.resolveVisibility5(visibility);
      return this;
   }

   public SettingGroup resolveSettingGroup6(String text) {
      this.text = text;
      return this;
   }
}
