package org.phobos.core.gui.element;

import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.Setting;

public abstract class SettingElement<V, S extends Setting<V>> extends GuiElement {
   public final S setting;
   public final String text2;
   public final ModuleElement moduleElement;

   public S getSetting() {
      return this.setting;
   }

   public SettingElement(
      GuiManager guiManager,
      GuiPanel guiPanel,
      float f,
      float f2,
      float f3,
      float f4,
      ModuleElement moduleElement,
      String text,
      S setting
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement);
      this.setting = setting;
      this.moduleElement = moduleElement;
      this.text2 = text;
   }

   public V getValue() {
      return this.setting.getValue();
   }

   public String getText3() {
      return this.text2;
   }

   public void setValue(V object) {
      this.setting.setValue(object);
   }

   public ModuleElement getModuleElement() {
      return this.moduleElement;
   }

   @Override
   public void handleDouble7(double d, double d2, boolean flag) {
      super.handleDouble7(d, d2, flag);
      if (!this.isFlag7() || this.moduleElement != null && (this.moduleElement.isFlag7() || !this.moduleElement.isFlag10())) {
         this.getGuiManager().handleObject(this);
      } else {
         this.getGuiManager().handleText(this.setting.getText(), this);
      }
   }
}
