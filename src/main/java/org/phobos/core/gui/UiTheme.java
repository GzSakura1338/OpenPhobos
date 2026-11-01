package org.phobos.core.gui;

import java.awt.Color;
import org.phobos.module.client.UIModule;

public class UiTheme {
   public final Color color = new Color(0, 0, 0, 0);

   public Color getColor() {
      return UIModule.getUIModule().backgroundColor.getValue();
   }

   public Color getColor2() {
      return UIModule.getUIModule().glowColor.getValue();
   }

   public GuiManager.UIShader getUIShader() {
      return UIModule.getUIModule().switchesShader.getValue();
   }

   public Color getColor3() {
      return UIModule.getUIModule().itemsColor.getValue();
   }

   public GuiManager.UIShader getUIShader2() {
      return UIModule.getUIModule().modulesShader.getValue();
   }

   public GuiManager.UIShader getUIShader3() {
      return UIModule.getUIModule().slidersShader.getValue();
   }

   public Color getColor4() {
      return UIModule.getUIModule().switchesEnabled.getValue();
   }

   public Color getColor5() {
      return UIModule.getUIModule().inputsColor.getValue();
   }

   public Color getColor6() {
      return UIModule.getUIModule().slidersColor.getValue();
   }

   public GuiManager.UIShader getUIShader4() {
      return UIModule.getUIModule().inputsShader.getValue();
   }

   public GuiManager.UIShader getUIShader5() {
      return UIModule.getUIModule().bindsShader.getValue();
   }

   public GuiManager.UIShader getUIShader6() {
      return UIModule.getUIModule().backgroundShader.getValue();
   }

   public Color getColor7() {
      return UIModule.getUIModule().bindsColor.getValue();
   }

   public Color getColor8() {
      return UIModule.getUIModule().pickersColor.getValue();
   }

   public Color getColor9() {
      return UIModule.getUIModule().modulesDisabled.getValue();
   }

   public Color getColor10() {
      return UIModule.getUIModule().text.getValue();
   }

   public GuiManager.UIShader getUIShader7() {
      return UIModule.getUIModule().categoriesShader.getValue();
   }

   public Color getColor11() {
      return UIModule.getUIModule().outlineColor.getValue();
   }

   public Color getColor12() {
      return UIModule.getUIModule().switchesDisabled.getValue();
   }

   public Color getColor13() {
      return UIModule.getUIModule().modulesEnabled.getValue();
   }

   public GuiManager.UIShader getUIShader8() {
      return UIModule.getUIModule().selectorsShader.getValue();
   }

   public GuiManager.UIShader getUIShader9() {
      return UIModule.getUIModule().itemsShader.getValue();
   }

   public Color getColor14() {
      return UIModule.getUIModule().selectorsColor.getValue();
   }

   public GuiManager.UIShader getUIShader10() {
      return UIModule.getUIModule().pickersShader.getValue();
   }

   public Color getColor15() {
      return UIModule.getUIModule().categoriesColor.getValue();
   }
}
