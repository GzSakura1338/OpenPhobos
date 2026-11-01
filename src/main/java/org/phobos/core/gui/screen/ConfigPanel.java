package org.phobos.core.gui.screen;

import java.util.List;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TimeGate;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.config.ConfigMeta;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.config.ProgressState;

public class ConfigPanel extends GuiPanel {
   public final FloatAnimator floatAnimator;
   public final FloatAnimator floatAnimator2 = new FloatAnimator();
   public final TimeGate.Normal normal;
   public final boolean flag;

   public TimeGate.Normal getNormal() {
      return this.normal;
   }

   public ConfigEntryElement resolveConfigEntryElement(ConfigEntryElement configEntryElement, ConfigMeta configMeta) {
      ConfigEntryElement configEntryElement2 = this.resolveGuiElement3(configEntryElement);
      if (configMeta.isFlag()) {
         int i = 0;

         int i2;
         for (i2 = 0; i2 < this.getGuiElementList2().size(); i2++) {
            GuiElement guiElement = this.getGuiElementList2().get(i2);
            if (!this.testGuiElement(guiElement)) {
               break;
            }
         }

         int i3 = this.computeList(this.getGuiElementList2(), configEntryElement, i, i2);
         this.getGuiElementList2().add(i3, configEntryElement2);
         return configEntryElement2;
      } else if (configMeta.check()) {
         int i4 = 0;
         int i5 = 0;

         while (i4 < this.getGuiElementList2().size()) {
            GuiElement guiElement2 = this.getGuiElementList2().get(i4);
            if (!this.testGuiElement(guiElement2)) {
               break;
            }

            i4++;
         }

         for (i5 = i4; i5 < this.getGuiElementList2().size(); i5++) {
            GuiElement guiElement3 = this.getGuiElementList2().get(i5);
            if (!this.testGuiElement2(guiElement3)) {
               break;
            }
         }

         int i6 = this.computeList(this.getGuiElementList2(), configEntryElement, i4, i5);
         this.getGuiElementList2().add(i6, configEntryElement2);
         return configEntryElement2;
      } else {
         int i7;
         for (i7 = 0; i7 < this.getGuiElementList2().size(); i7++) {
            GuiElement guiElement4 = this.getGuiElementList2().get(i7);
            if (!this.testGuiElement(guiElement4) && !this.testGuiElement2(guiElement4)) {
               break;
            }
         }

         int i8 = this.computeList(this.getGuiElementList2(), configEntryElement, i7, this.getGuiElementList2().size());
         this.getGuiElementList2().add(i8, configEntryElement2);
         return configEntryElement2;
      }
   }

   public FloatAnimator getFloatAnimator2() {
      return this.floatAnimator2;
   }

   @Override
   public void handleInt2(int i, int i2, int i3) {
      super.handleInt2(i, i2, i3);
      if (this.testDouble4(MenuNavigation.value(), MenuNavigation.value2())) {
         if (i == 294 && this.normal.testLong2(1000L)) {
            if (this.flag) {
               ConfigManager.run9();
               return;
            }

            ClientHolder.getPhobosClient().getConfigManager().run();
         }
      }
   }

   public boolean isFlag2() {
      return this.flag;
   }

   public ConfigPanel(ConfigGuiManager configGuiManager, String text, boolean flag) {
      super(configGuiManager, text, 0.0F, 0.0F, 216.0F, 15.0F);
      this.floatAnimator = new FloatAnimator(1.0F, 1.0F);
      this.normal = new TimeGate.Normal();
      this.flag = flag;
   }

   public void handleConfigEntryElement(ConfigEntryElement configEntryElement) {
      this.handleGuiElement3(configEntryElement);
      GuiElement guiElement5 = this.resolveGuiElement3(configEntryElement);
      if (configEntryElement.getConfigInfo().isFlag()) {
         int i = 0;

         int i2;
         for (i2 = 0; i2 < this.getGuiElementList2().size(); i2++) {
            GuiElement guiElement = this.getGuiElementList2().get(i2);
            if (!this.testGuiElement(guiElement)) {
               break;
            }
         }

         int i3 = this.computeList(this.getGuiElementList2(), configEntryElement, i, i2);
         this.getGuiElementList2().add(i3, guiElement5);
      } else if (configEntryElement.getConfigInfo().check()) {
         int i4 = 0;
         int i5 = 0;

         while (i4 < this.getGuiElementList2().size()) {
            GuiElement guiElement2 = this.getGuiElementList2().get(i4);
            if (!this.testGuiElement(guiElement2)) {
               break;
            }

            i4++;
         }

         for (i5 = i4; i5 < this.getGuiElementList2().size(); i5++) {
            GuiElement guiElement3 = this.getGuiElementList2().get(i5);
            if (!this.testGuiElement2(guiElement3)) {
               break;
            }
         }

         int i6 = this.computeList(this.getGuiElementList2(), configEntryElement, i4, i5);
         this.getGuiElementList2().add(i6, guiElement5);
      } else {
         int i7;
         for (i7 = 0; i7 < this.getGuiElementList2().size(); i7++) {
            GuiElement guiElement4 = this.getGuiElementList2().get(i7);
            if (!this.testGuiElement(guiElement4) && !this.testGuiElement2(guiElement4)) {
               break;
            }
         }

         int i8 = this.computeList(this.getGuiElementList2(), configEntryElement, i7, this.getGuiElementList2().size());
         this.getGuiElementList2().add(i8, guiElement5);
      }
   }

   @Nullable
   public ConfigEntryElement resolveConfigEntryElement2(String text) {
      MutableObject<ConfigEntryElement> mutableObject = new MutableObject();

      for (GuiElement guiElement : this.getGuiElementList2()) {
         if (guiElement instanceof ConfigEntryElement configEntryElement && configEntryElement.describe().equals(text)) {
            mutableObject.setValue(configEntryElement);
            break;
         }
      }

      if (mutableObject.getValue() == null) {
         return null;
      }

      ConfigEntryElement configEntryElement2 = (ConfigEntryElement)mutableObject.getValue();
      this.handleGuiElement3(configEntryElement2);
      return configEntryElement2;
   }

   public int computeList(List<GuiElement> list, ConfigEntryElement configEntryElement, int i, int i2) {
      String text = configEntryElement.describe();

      for (int i3 = i; i3 < i2; i3++) {
         GuiElement guiElement = list.get(i3);
         if (guiElement instanceof ConfigEntryElement configEntryElement2 && String.CASE_INSENSITIVE_ORDER.compare(text, configEntryElement2.describe()) < 0) {
            return i3;
         }
      }

      return i2;
   }

   public ProgressState getProgressState() {
      return this.flag ? ConfigManager.getProgressState() : ConfigManager.getProgressState2();
   }

   public boolean testGuiElement(GuiElement guiElement) {
      return guiElement instanceof ConfigEntryElement configEntryElement && configEntryElement.isFlag();
   }

   @Override
   public void run() {
      this.floatAnimator2.setFloatValue2(0.0F);
      this.floatAnimator.setFloatValue2(1.0F);
   }

   public FloatAnimator getFloatAnimator3() {
      return this.floatAnimator;
   }

   public ConfigEntryElement resolveConfigEntryElement3(ConfigEntryElement configEntryElement) {
      ConfigEntryElement configEntryElement2 = this.resolveGuiElement3(configEntryElement);
      int i = this.computeList(this.getGuiElementList2(), configEntryElement, 0, this.getGuiElementList2().size());
      this.getGuiElementList2().add(i, configEntryElement2);
      return configEntryElement2;
   }

   public boolean testGuiElement2(GuiElement guiElement) {
      return guiElement instanceof ConfigEntryElement configEntryElement && configEntryElement.check();
   }

   @Override
   public void handleDouble2(double d, double d2) {
      super.handleDouble2(d, d2);
      this.floatAnimator.setFloatValue(this.floatAnimator2.getFloatValue2() >= 0.95F ? 0.0F : 1.0F);
      this.floatAnimator.handleFloat3(10.0F, 0.02F);
      ProgressState progressState = this.getProgressState();
      float f = progressState.value();
      if (progressState.check()) {
         this.run();
      }

      this.floatAnimator2.setFloatValue(f);
      this.floatAnimator2.handleFloat3(10.0F, 0.1F);
      this.handleDouble11(
         0.0,
         this.getFloatValue4(),
         this.getFloatValue3() * this.floatAnimator2.getFloatValue2(),
         this.getFloatValue4() + 2.0F,
         ColorUtils.createColor6(this.getGuiManager().getUiTheme().getColor6(), this.floatAnimator.getFloatValue2()),
         this.getGuiManager().getUiTheme().getUIShader3()
      );
   }

   @Override
   public void run5() {
      if (this.flag) {
         this.run();
      }

      super.run5();
   }
}
