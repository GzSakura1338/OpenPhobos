package org.phobos.core.gui.screen;

import org.phobos.api.GuiGraphics;
import org.phobos.core.gui.GuiManager;

public class HudGuiManager extends GuiManager {
   public HudPanel resolveGuiPanel(String text) {
      HudPanel hudPanel = new HudPanel(this, text);
      this.getGuiPanelList().add(hudPanel);
      return hudPanel;
   }

   public HudGuiManager(GuiGraphics guiGraphics) {
      super(guiGraphics);
   }
}
