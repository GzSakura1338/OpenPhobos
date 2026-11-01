package org.phobos.core.gui.screen;

import org.phobos.api.GuiGraphics;
import org.phobos.core.gui.GuiManager;

public class ConfigGuiManager extends GuiManager {
   public ConfigGuiManager(GuiGraphics guiGraphics) {
      super(guiGraphics);
   }

   public ConfigPanel resolveConfigPanel(String text, boolean flag) {
      ConfigPanel configPanel = new ConfigPanel(this, text, flag);
      this.getGuiPanelList().add(configPanel);
      return configPanel;
   }
}
