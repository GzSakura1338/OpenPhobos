package org.phobos.core.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TextFormatter;
import org.phobos.api.NameCodec;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.setting.item.ItemGroupSet;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.element.SetElement;

public class SetSetting<T> extends Setting<Set<T>> {
   public final NameCodec<T> nameCodec;
   public final ItemGroupSet<T> itemGroupSet;
   public int limit = -1;
   public final Function<T, ItemStack> function;
   public final Function<T, String> function2;
   public final TextFormatter textFormatter;

   public SetSetting(String text, NameCodec<T> nameCodec, Function<T, ItemStack> function, Function<T, String> function2, TextFormatter textFormatter) {
      this(text, new HashSet<>(), nameCodec, function, function2, textFormatter);
   }

   public ItemGroupSet<T> getItemGroupSet() {
      return this.itemGroupSet;
   }

   public int getLimit() {
      return this.limit;
   }

   public SetSetting(
      String text, Set<T> set, NameCodec<T> nameCodec, Function<T, ItemStack> function, Function<T, String> function2, TextFormatter textFormatter
   ) {
      this(text, "", set, nameCodec, function, function2, textFormatter);
   }

   public SetSetting<T> resolveSetSetting(int i) {
      this.limit = i;
      return this;
   }

   public Function<T, String> getFunction2() {
      return this.function2;
   }

   public Function<T, ItemStack> getFunction() {
      return this.function;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new SetElement<>(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public List<T> resolveList(String text) {
      T object = this.nameCodec.resolveObject(text);
      return object != null ? List.of(object) : this.itemGroupSet.resolveList(text);
   }

   public String describeText(String text) {
      String text2 = this.itemGroupSet.describeText(text);
      return text2 != null ? text2 : this.textFormatter.describeText(text);
   }

   @Override
   public boolean testObject3(Object object) {
      try {
         T object2 = (T)object;
         return this.getValue().contains(object2);
      } catch (ClassCastException classCastException) {
         return false;
      }
   }

   public NameCodec<T> getNameCodec() {
      return this.nameCodec;
   }

   public boolean check() {
      return this.limit == -1 || this.getValue().size() < this.limit;
   }

   public SetSetting(
      String text,
      @Nullable String text2,
      Set<T> set,
      NameCodec<T> nameCodec,
      Function<T, ItemStack> function,
      Function<T, String> function2,
      TextFormatter textFormatter
   ) {
      super(text, text2, set);
      this.itemGroupSet = new ItemGroupSet<>();
      this.nameCodec = nameCodec;
      this.function = function;
      this.function2 = function2;
      this.textFormatter = textFormatter;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      Set<T> set = new HashSet<>();

      for (JsonElement jsonElement2 : jsonArray) {
         T object = this.nameCodec.resolveObject(jsonElement2.getAsString());
         if (object != null) {
            set.add(object);
         }
      }

      this.setValue(set);
   }

   public TextFormatter getTextFormatter() {
      return this.textFormatter;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonArray jsonArray = new JsonArray();

      for (T object : this.getValue()) {
         if (object != null) {
            jsonArray.add(this.nameCodec.describeBlock(object));
         }
      }

      return jsonArray;
   }
}
