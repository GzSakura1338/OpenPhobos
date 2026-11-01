package org.phobos.core.setting;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.phobos.core.gui.element.BindElement;
import org.phobos.core.bind.BindKey;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.gui.ModuleElement;

public class BindSetting extends Setting<KeyCombination> {
   public BindSetting(String text) {
      this(text, KeyCombination.createKeyCombination());
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new BindElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      KeyCombination keyCombination = new KeyCombination();
      keyCombination.handleJsonElement(jsonElement);
      this.setValue(keyCombination);
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      BindKey bindKey = ClientHolder.getPhobosClient().getBindManager().resolveBindKey2(texts[0]);
      if (bindKey == null) {
         return false;
      }

      this.setValue(new KeyCombination(bindKey));
      return true;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return this.getValue().createJsonPrimitive();
   }

   public BindSetting(String text, KeyCombination keyCombination) {
      super(text, "", keyCombination);
   }
}
