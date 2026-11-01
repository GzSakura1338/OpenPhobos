package org.phobos.core.gui;

public abstract class ElementGroup<T extends GuiElement> extends GuiElement {
   public final T[] guiElementArray;

   @SafeVarargs
   public ElementGroup(
      GuiManager guiManager, GuiPanel guiPanel, float f, float f2, float f3, float f4, ModuleElement moduleElement, T... guiElements
   ) {
      super(guiManager, guiPanel, f, f2, f3, f4, moduleElement);
      this.guiElementArray = guiElements;
   }

   public T[] getGuiElementArray() {
      return this.guiElementArray;
   }
}
