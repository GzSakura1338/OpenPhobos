package org.phobos.core.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.phobos.api.EntryContainer;
import org.phobos.api.Visibility;

public class SettingSubsection implements EntryContainer<SettingGroup>, Visibility {
   public String name;
   public final List<Setting<?>> settingList;
   public Visibility visibility;
   public Setting<Boolean> booleanValueSetting;
   public final List<SettingGroup> settingGroupList = new ArrayList<>();

   public void setBooleanValueSetting(Setting<Boolean> setting) {
      this.booleanValueSetting = setting;
   }

   public List<Setting<?>> getSettingList() {
      return this.settingList;
   }

   public Visibility getVisibility() {
      return this.visibility;
   }

   @Override
   public boolean isFlag8() {
      return this.visibility.isFlag8();
   }

   public SettingSubsection() {
      this.settingList = new ArrayList<>();
      this.visibility = Visibility.INSTANCE;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String text) {
      this.name = text;
   }

   public void setVisibility(Visibility visibility) {
      this.visibility = visibility;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   @Override
   public List<SettingGroup> getSettingGroupList() {
      return this.settingGroupList;
   }

   public static class Builder {
      public final SettingSubsection settingSubsection = new SettingSubsection();
      public final List<SettingSubsection> settingSubsectionList;
      public final List<SettingGroup> settingGroupList;

      public SettingSubsection.Builder resolveBuilder(Setting<?>... settings) {
         Collections.addAll(this.settingSubsection.getSettingList(), settings);
         return this;
      }

      public SettingSubsection.Builder resolveBuilder2(List<SettingGroup> list) {
         this.settingSubsection.getSettingGroupList().addAll(list);
         return this;
      }

      public SettingSubsection.Builder resolveBuilder3(Visibility visibility) {
         this.settingSubsection.setVisibility(visibility);
         return this;
      }

      public Builder(List<SettingSubsection> list, List<SettingGroup> list2) {
         this.settingSubsectionList = list;
         this.settingGroupList = list2;
      }

      public SettingSubsection getSettingSubsection() {
         this.settingSubsectionList.add(this.settingSubsection);
         this.settingGroupList.removeIf(this.settingSubsection.settingGroupList::contains);
         return this.settingSubsection;
      }

      public SettingSubsection.Builder resolveBuilder4(String text) {
         this.settingSubsection.setName(text);
         return this;
      }

      public SettingSubsection.Builder resolveBuilder5(SettingGroup... settingGroups) {
         Collections.addAll(this.settingSubsection.getSettingGroupList(), settingGroups);
         return this;
      }

      public SettingSubsection.Builder resolveBuilder6(Setting<Boolean> setting) {
         this.settingSubsection.setBooleanValueSetting(setting);
         this.settingSubsection.setName(setting.getName());
         return this;
      }
   }
}
