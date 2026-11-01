package org.phobos.core.gui.screen;

import org.phobos.api.GuiGraphics;
import org.phobos.core.gui.GuiManager;

public class ScriptGuiManager extends GuiManager {
   public ScriptPanel resolveScriptPanel(String text, boolean flag) {
      ScriptPanel scriptPanel = new ScriptPanel(this, text, flag);
      this.getGuiPanelList().add(scriptPanel);
      return scriptPanel;
   }

   public ScriptGuiManager(GuiGraphics guiGraphics) {
      super(guiGraphics);
   }
}
