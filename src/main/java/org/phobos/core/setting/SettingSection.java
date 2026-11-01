package org.phobos.core.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.phobos.api.EntryContainer;
import org.phobos.api.Named;

public class SettingSection implements EntryContainer<SettingGroup>, Named {
   public String name;
   public final List<Setting<?>> settingList;
   public final List<SettingGroup> settingGroupList;
   public final List<SettingSubsection> settingSubsectionList = new ArrayList<>();
   public Setting<Boolean> booleanValueSetting;

   public List<SettingSubsection> getSettingSubsectionList() {
      return this.settingSubsectionList;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public List<Setting<?>> getSettingList() {
      return this.settingList;
   }

   public void setBooleanValueSetting(Setting<Boolean> setting) {
      this.booleanValueSetting = setting;
   }

   public void setName(String text) {
      this.name = text;
   }

   @Override
   public List<SettingGroup> getSettingGroupList() {
      return this.settingGroupList;
   }

   public SettingSection() {
      this.settingGroupList = new ArrayList<>();
      this.settingList = new ArrayList<>();
   }

   public static class Builder {
      public final List<SettingGroup> settingGroupList;
      public final List<SettingSubsection> settingSubsectionList;
      public final List<SettingSection> settingSectionList;
      public final SettingSection settingSection = new SettingSection();

      public SettingSection.Builder resolveBuilder(List<SettingSubsection> list) {
         this.settingSection.getSettingSubsectionList().addAll(list);
         return this;
      }

      public SettingSection getSettingSection() {
         this.settingSectionList.add(this.settingSection);
         this.settingSubsectionList.removeIf(this.settingSection.settingSubsectionList::contains);
         this.settingGroupList.removeIf(this.settingSection.settingGroupList::contains);
         return this.settingSection;
      }

      public SettingSection.Builder resolveBuilder2(List<SettingGroup> list) {
         this.settingSection.getSettingGroupList().addAll(list);
         return this;
      }

      public SettingSection.Builder resolveBuilder3(Setting<?>... settings) {
         Collections.addAll(this.settingSection.getSettingList(), settings);
         return this;
      }

      public SettingSection.Builder resolveBuilder4(SettingSubsection... settingSubsections) {
         Collections.addAll(this.settingSection.getSettingSubsectionList(), settingSubsections);
         return this;
      }

      public SettingSection.Builder resolveBuilder5(String text) {
         this.settingSection.setName(text);
         return this;
      }

      public SettingSection.Builder resolveBuilder6(Setting<Boolean> setting) {
         this.settingSection.setBooleanValueSetting(setting);
         this.settingSection.setName(setting.getName());
         return this;
      }

      public SettingSection.Builder resolveBuilder7(SettingGroup... settingGroups) {
         Collections.addAll(this.settingSection.getSettingGroupList(), settingGroups);
         return this;
      }

      public Builder(List<SettingSection> list, List<SettingSubsection> list2, List<SettingGroup> list3) {
         this.settingSectionList = list;
         this.settingSubsectionList = list2;
         this.settingGroupList = list3;
      }
   }
}
