package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.element.StringElement;

public class StringSetting extends Setting<String> {
   public int limit = Integer.MAX_VALUE;
   public Predicate<Character> characterPredicate;

   public StringSetting(String text, String text2) {
      this(text, "", text2);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.setValue(jsonElement.getAsString());
   }

   public int getLimit() {
      return this.limit;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      this.setValue(String.join(" ", texts));
      return true;
   }

   public StringSetting resolveStringSetting(int i) {
      this.limit = i;
      return this;
   }

   public StringSetting(String text, @Nullable String text2, String text3) {
      super(text, text2, text3);
   }

   public Predicate<Character> getPacketPredicate() {
      return this.characterPredicate;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new StringElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public StringSetting resolveStringSetting2(Predicate<Character> predicate) {
      this.characterPredicate = predicate;
      return this;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return new JsonPrimitive(this.getValue());
   }
}
