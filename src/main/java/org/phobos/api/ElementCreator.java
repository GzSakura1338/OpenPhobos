package org.phobos.api;

import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.GuiManager;

public interface ElementCreator {
   GuiElement createEnumElement(GuiManager var1, GuiPanel var2, String var3, ModuleElement var4);
}
