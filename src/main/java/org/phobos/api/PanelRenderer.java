package org.phobos.api;

import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.InteractiveElement;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.HoverRegion;
import org.phobos.core.gui.CursorUtils;

public interface PanelRenderer {
   default <W extends GuiPanel> void handleGuiPanel(W guiPanel, double d, double d2, float f, float f2) {
      if (guiPanel != null) {
         if (!guiPanel.getModuleElementList().isEmpty()) {
            for (ModuleElement moduleElement : guiPanel.getModuleElementList()) {
               if (!moduleElement.isFlag4()) {
                  moduleElement.setFloatValue(f + 14.0F);
                  moduleElement.setFloatValue2(guiPanel.getFloatValue2() + 2.0F);
                  if (moduleElement.isFlag8()) {
                     if (!guiPanel.isFlag5()) {
                        CursorUtils.Style style = moduleElement.getStyle();
                        if (style != CursorUtils.Style.Default) {
                           HoverRegion hoverRegion = moduleElement.getHoverRegion();
                           if (hoverRegion.testDouble(d, d2)) {
                              guiPanel.handleStyle(style);
                           }
                        }
                     }

                     if (!guiPanel.isFlag72() && !guiPanel.isFlag11()) {
                        moduleElement.handleDouble4(d, d2);
                        guiPanel.getGuiManager().getDrawStats().run2();
                     }

                     f += moduleElement.getFloatValue33() + f2;
                  }
               }
            }
         }

         if (!guiPanel.getGuiElementList2().isEmpty()) {
            for (GuiElement guiElement : guiPanel.getGuiElementList2()) {
               guiElement.setFloatValue(f + 14.0F);
               guiElement.setFloatValue2(guiPanel.getFloatValue2() + 2.0F);
               if (guiElement.isFlag8()) {
                  if (!guiPanel.isFlag5()) {
                     CursorUtils.Style style2 = guiElement.getStyle();
                     if (style2 != CursorUtils.Style.Default) {
                        HoverRegion hoverRegion2 = guiElement.getHoverRegion();
                        if (hoverRegion2.testDouble(d, d2)) {
                           guiPanel.handleStyle(style2);
                        }
                     }
                  }

                  if (!guiPanel.isFlag72() && !guiPanel.isFlag11()) {
                     guiElement.handleDouble4(d, d2);
                     guiPanel.getGuiManager().getDrawStats().run2();
                  }

                  if (guiElement instanceof InteractiveElement interactiveElement) {
                     f += interactiveElement.getFloatValue33() + f2;
                  } else {
                     f += guiElement.getFloatValue4() + f2;
                  }
               }
            }
         }
      }
   }
}
