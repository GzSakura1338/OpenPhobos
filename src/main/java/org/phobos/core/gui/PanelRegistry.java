package org.phobos.core.gui;

import java.util.ArrayList;
import java.util.List;
import org.phobos.api.PanelHolder;

public class PanelRegistry {
   public static final PanelRegistry INSTANCE = new PanelRegistry();
   public final List<PanelHolder> panelHolderList = new ArrayList<>();

   public void handlePanelHolder(PanelHolder panelHolder) {
      synchronized (this.panelHolderList) {
         this.panelHolderList.add(panelHolder);
      }
   }

   public void handlePanelHolder2(PanelHolder panelHolder) {
      for (GuiPanel guiPanel : panelHolder.getGuiPanelList()) {
         for (GuiElement guiElement : guiPanel.getColorPickerList()) {
            guiElement.run12();
         }
      }
   }

   public void run() {
      synchronized (this.panelHolderList) {
         for (PanelHolder panelHolder : this.panelHolderList) {
            for (GuiPanel guiPanel : panelHolder.getGuiPanelList()) {
               for (GuiElement guiElement : guiPanel.getColorPickerList()) {
                  guiElement.run12();
               }
            }
         }
      }
   }

   public static PanelRegistry getINSTANCE() {
      return INSTANCE;
   }
}
