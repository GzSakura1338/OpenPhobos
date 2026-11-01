package org.phobos.core.gui.screen;

import org.phobos.api.GuiGraphics;
import org.phobos.core.gui.GuiManager;

public class InfoGuiManager extends GuiManager {
   public InfoGuiManager(GuiGraphics guiGraphics) {
      super(guiGraphics);
   }

   public InfoPanel resolveGuiPanel(String text) {
      InfoPanel infoPanel = new InfoPanel(this, text);
      this.getGuiPanelList().add(infoPanel);
      return infoPanel;
   }
}
