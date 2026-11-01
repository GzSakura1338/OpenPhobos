package org.phobos.core.gui;

import java.awt.Color;
import org.phobos.api.GuiGraphics;
import org.phobos.module.client.UIModule;

public class DrawStats {
   public int intValue;
   public int intValue2;
   public int intValue3;
   public int intValue4;

   public void handleGuiGraphics(GuiGraphics guiGraphics) {
      if (UIModule.getUIModule().debug.getValue()) {
         String text = "cut=%s, elements=%s, drawCalls=%s, updates=%s".formatted(this.intValue2, this.intValue3, this.intValue, this.intValue4);
         guiGraphics.handleText(text, 2.0F, 2.0F, Color.WHITE, 1.0F, false);
      }
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public void run() {
      this.intValue++;
   }

   public void run2() {
      this.intValue4++;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public void run3() {
      this.setIntValue2(0);
      this.setIntValue3(0);
      this.setIntValue(0);
      this.setIntValue4(0);
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void run4() {
      this.intValue2++;
   }

   public void setIntValue4(int i) {
      this.intValue4 = i;
   }

   public void setIntValue3(int i) {
      this.intValue3 = i;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void run5() {
      this.intValue3++;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }
}
