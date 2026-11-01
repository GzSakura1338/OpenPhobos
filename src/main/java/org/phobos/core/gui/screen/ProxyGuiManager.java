package org.phobos.core.gui.screen;

import org.phobos.api.GuiGraphics;
import org.phobos.core.gui.GuiManager;

public class ProxyGuiManager extends GuiManager {
   public ProxyGuiManager(GuiGraphics guiGraphics) {
      super(guiGraphics);
   }

   public ProxyPanel resolveProxyPanel(String text, boolean flag) {
      ProxyPanel proxyPanel = new ProxyPanel(this, text, flag);
      this.getGuiPanelList().add(proxyPanel);
      return proxyPanel;
   }
}
