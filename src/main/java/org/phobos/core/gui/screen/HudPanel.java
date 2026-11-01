package org.phobos.core.gui.screen;

import org.phobos.core.gui.GuiPanel;
import org.phobos.core.hud.HudElement;
import org.phobos.core.gui.ModuleElement;

public class HudPanel extends GuiPanel {
   @Override
   public void handleDouble4(double d, double d2) {
      boolean flag = this.isFlag7();
      if (this.getGuiGraphics().testInt(1)) {
         this.flag5 = flag;
      } else {
         if (this.flag5 && flag) {
            this.flag4 = !this.flag4;
         }

         this.flag5 = false;
      }

      if (!this.flag10) {
         if (this.getGuiGraphics().testInt(0)) {
            if ((this.getGuiManager().getDragCapture() == null || this.getGuiManager().getDragCapture() == this) && HudScreen.hudScreen.check()) {
               if (!this.flag3 && flag) {
                  this.floatValue14 = (float)(d - this.getFloatValue2());
                  this.floatValue2 = (float)(d2 - this.getFloatValue());
                  this.flag3 = true;
                  this.getGuiManager().setDragCapture(this);
               }

               if (this.flag3) {
                  this.handleDouble5(d, d2);
               }
            }
         } else if (this.flag3) {
            this.handleDouble5(d, d2);
            this.flag3 = false;
            if (this.getGuiManager().getDragCapture() == this) {
               this.getGuiManager().setDragCapture(null);
            }
         }
      }
   }

   public ModuleElement resolveModuleElement4(HudElement hudElement) {
      return this.resolveModuleElement3(
         new ModuleElement(
            this.getGuiManager(), this, hudElement, this.getFloatValue2() + 2.0F, this.getIntValue() * 14 + 14, this.getFloatValue3() - 4.0F, 15.0F
         )
      );
   }

   public HudPanel(HudGuiManager hudGuiManager, String text) {
      super(hudGuiManager, text);
   }
}
