package org.phobos.api;

import java.util.List;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.SettingGroup;

public interface SettingPanelBuilder {
   default <T extends EntryContainer<SettingGroup>> void handleEntryContainer(
      T entryContainer, GuiPanel guiPanel, ModuleElement moduleElement, List<Setting<?>> list
   ) {
      for (SettingGroup settingGroup : entryContainer.getSettingGroupList()) {
         ModuleElement moduleElement2 = guiPanel.resolveModuleElement2(
            settingGroup.getName(), moduleElement, settingGroup.getBooleanValueSetting()
         );
         moduleElement2.setGuiElementPredicate(guiElement -> settingGroup.getVisibility().isFlag8() && moduleElement.getModuleElement().isFlag8());
         this.handleList(settingGroup.getSettingList(), guiPanel, moduleElement2);
         list.removeAll(settingGroup.getSettingList());
         Setting<Boolean> setting;
         if ((setting = settingGroup.getBooleanValueSetting()) != null) {
            list.remove(setting);
         }
      }
   }

   default void handleList(List<Setting<?>> list, GuiPanel guiPanel, ModuleElement moduleElement) {
      for (Setting<?> setting : list) {
         GuiElement guiElement = guiPanel.resolveGuiElement2(setting, moduleElement);
         if (guiElement != null) {
            guiElement.setGuiElementPredicate(guiElementx -> setting.getVisibility().isFlag8() && moduleElement.getModuleElement().isFlag8());
         }
      }
   }
}
