package org.phobos.core.gui.element;

import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.InteractiveElement;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.setting.Setting;

public abstract class SettingValueElement<V, S extends Setting<V>> extends InteractiveElement {
   public final ModuleElement moduleElement;
   public final String text;
   public final S setting;

   @Override
   public void handleDouble7(double d, double d2, boolean flag) {
      super.handleDouble7(d, d2, flag);
      if (!this.isFlag7() || this.moduleElement != null && (this.moduleElement.isFlag7() || !this.moduleElement.isFlag10())) {
         this.getGuiManager().handleObject(this);
      } else {
         this.getGuiManager().handleText(this.setting.getText(), this);
      }
   }

   public void setValue(V object) {
      this.setting.setValue(object);
   }

   public S getSetting() {
      return this.setting;
   }

   public SettingValueElement(
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
      this.text = text;
   }

   public V getValue() {
      return this.setting.getValue();
   }

   public ModuleElement getModuleElement() {
      return this.moduleElement;
   }

   public String getText3() {
      return this.text;
   }
}
