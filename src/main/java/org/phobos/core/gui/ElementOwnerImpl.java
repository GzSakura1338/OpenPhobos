package org.phobos.core.gui;

import org.phobos.api.ElementOwner;

public class ElementOwnerImpl<E extends GuiElement> implements ElementOwner<E> {
   public final E guiElement;

   public ElementOwnerImpl(E guiElement) {
      this.guiElement = guiElement;
   }

   @Override
   public E getGuiElement() {
      return this.guiElement;
   }
}
