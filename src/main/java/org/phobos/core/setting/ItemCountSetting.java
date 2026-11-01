package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import org.phobos.api.KeyValue;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.element.ItemCountElement;
import org.phobos.core.gui.ModuleElement;

public class ItemCountSetting extends Setting<KeyValue<Item, Integer>> {
   public int intValue;
   public boolean flag;
   public final int intValue2;
   public int intValue3;
   public int intValue4 = 1;

   public ItemCountSetting(String text, Item item, int i) {
      super(text, null, KeyValue.createValue(item, i));
      this.intValue3 = Integer.MIN_VALUE;
      this.intValue = Integer.MAX_VALUE;
      this.flag = false;
      this.intValue2 = i;
   }

   public int computeInt(int i) {
      return Math.clamp(i, this.intValue3, this.intValue);
   }

   public int getIntValue() {
      return this.intValue;
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return new JsonPrimitive(this.getValue().getObject2());
   }

   public ItemCountSetting getItemCountSetting() {
      this.flag = true;
      return this;
   }

   public ItemCountSetting resolveItemCountSetting(int i) {
      this.intValue3 = i;
      return this;
   }

   public ItemCountSetting resolveItemCountSetting2(int i) {
      this.intValue = i;
      return this;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new ItemCountElement(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      this.setValue(KeyValue.createValue(this.getObject().getObject(), jsonElement.getAsInt()));
   }

   public ItemCountSetting resolveItemCountSetting3(int i) {
      this.intValue4 = i;
      return this;
   }

   public int getIntValue4() {
      return this.intValue4;
   }

   public int getIntValue2() {
      return this.intValue2;
   }
}
