package org.phobos.core.gui;

import org.phobos.api.ElementOwner;
import org.phobos.api.ElementSource;
import org.phobos.core.setting.Setting;

public abstract class ElementFactory<T, S extends Setting<T>, R extends ElementOwner<GuiElement>> implements ElementSource<ElementFactory<T, S, R>, R> {
   public abstract GuiElement getGuiElement();

   public R getObject() {
      return ElementOwner.createElementOwnerImpl(this.getGuiElement());
   }
}
