package org.phobos.api;

import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.ElementOwnerImpl;

public interface ElementOwner<E extends GuiElement> {
   static <S, T extends GuiElement> S createElementOwnerImpl(T guiElement) {
      return (S)(new ElementOwnerImpl<T>(guiElement));
   }

   E getGuiElement();

   default void handleFloat(float f, float f2, float f3, float f4) {
      GuiElement guiElement = this.getGuiElement();
      guiElement.setFloatValue2(f);
      guiElement.setFloatValue(f2);
      guiElement.setFloatValue3(f3);
      guiElement.setFloatValue4(f4);
   }
}
