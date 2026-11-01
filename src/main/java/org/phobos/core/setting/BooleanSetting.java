package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Visibility;
import org.phobos.core.gui.element.BooleanElement;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class BooleanSetting extends Setting<Boolean> {
   public BooleanSetting(String text) {
      this(text, "", false);
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new BooleanElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public BooleanSetting(String text, @Nullable String text2, Boolean booleanValue) {
      super(text, text2, booleanValue);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.setValue(jsonElement.getAsBoolean());
   }

   public BooleanSetting(String text, Boolean booleanValue) {
      this(text, "", booleanValue);
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      try {
         this.setValue(Boolean.parseBoolean(texts[0]));
         return true;
      } catch (Throwable throwable) {
         return false;
      }
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return new JsonPrimitive(this.getValue());
   }

   public Visibility resolveVisibility(Boolean... booleanValues) {
      return this::getValue;
   }
}
