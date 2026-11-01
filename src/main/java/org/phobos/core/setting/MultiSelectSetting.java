package org.phobos.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.Named;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;
import org.phobos.core.gui.element.MultiSelectElement;
import org.phobos.core.util.NullSafe;
import org.phobos.core.PhobosLog;

public class MultiSelectSetting<T> extends Setting<Map<T, Boolean>> {
   public final Map<String, T> textMap;
   public MultiSelectSetting.SelectSortType selectSortType;
   public final Map<T, String> map = new LinkedHashMap<>();
   public boolean flag;
   public final Class<? super T> valueClassValue;

   public MultiSelectSetting<T> resolveMultiSelectSetting(T object, String text) {
      return this.resolveMultiSelectSetting2(object, text, false);
   }

   public MultiSelectSetting<T> resolveMultiSelectSetting2(T object, String text, boolean flag) {
      this.getValue().put(object, flag);
      this.map.put(object, text);
      this.textMap.put(text, object);
      return this;
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonObject jsonObject = new JsonObject();

      for (Map.Entry<T, Boolean> entry : this.getValue().entrySet()) {
         NullSafe.handleObject(this.map.get(entry.getKey()), text -> jsonObject.addProperty(text, entry.getValue()));
      }

      return jsonObject;
   }

   public MultiSelectSetting<T> resolveMultiSelectSetting3(T object) {
      this.getValue().put(object, true);
      return this;
   }

   public MultiSelectSetting.SelectSortType getSelectSortType() {
      return this.selectSortType;
   }

   public void run() {
      if (this.flag) {
         MutableObject<T> mutableObject = new MutableObject();
         Set<T> set = new HashSet<>();

         for (Map.Entry<T, Boolean> entry : this.getValue().entrySet()) {
            T object = entry.getKey();
            if (entry.getValue()) {
               if (mutableObject.getValue() != null) {
                  set.add(object);
               } else {
                  mutableObject.setValue(object);
               }
            }
         }

         for (T object2 : set) {
            this.getValue().put(object2, false);
         }
      }
   }

   public MultiSelectSetting(String text, Class<T> classValue) {
      super(text, "", new LinkedHashMap<>(0));
      this.textMap = new LinkedHashMap<>();
      this.selectSortType = MultiSelectSetting.SelectSortType.Free;
      this.flag = false;
      this.valueClassValue = classValue;
   }

   public void setValue(Map<T, Boolean> map) {
   }

   public Map<T, String> getMap() {
      return this.selectSortType.sort(this.map, this.textMap);
   }

   public Map<T, String> getMap2() {
      return this.map;
   }

   public MultiSelectSetting<T> getMultiSelectSetting() {
      this.setFlag(true);
      return this;
   }

   @Override
   public boolean testString(@NotNull String... texts) {
      return false;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      for (String text : jsonObject.keySet()) {
         Boolean booleanValue = jsonObject.get(text).getAsBoolean();
         NullSafe.handleObject(this.textMap.get(text), object -> this.getValue().put((T)object, booleanValue));
      }
   }

   public MultiSelectSetting<T> resolveMultiSelectSetting4(MultiSelectSetting.SelectSortType selectSortType) {
      this.setSelectSortType(selectSortType);
      return this;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      return new MultiSelectElement<>(guiManager, guiPanel, 0.0F, 0.0F, 0.0F, 0.0F, moduleElement, text, this);
   }

   public Map<String, T> getTextMap() {
      return this.textMap;
   }

   public MultiSelectSetting<T> resolveMultiSelectSetting5(Consumer<MultiSelectSetting<T>> consumer) {
      consumer.accept(this);
      return this;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Class<? super T> getValueClassValue() {
      return this.valueClassValue;
   }

   @SafeVarargs
   public final MultiSelectSetting<T> resolveMultiSelectSetting6(T[] objects, Function<T, String>... functions) {
      for (T object : objects) {
         try {
            this.resolveMultiSelectSetting(
               object,
               object instanceof Named named
                  ? named.getName()
                  : (object instanceof Enum<?> enumValue ? enumValue.name() : functions[0].apply(object))
            );
         } catch (Throwable throwable) {
            PhobosLog.handleText("Settings", throwable.getMessage());
         }
      }

      return this;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public MultiSelectSetting<T> getMultiSelectSetting2() {
      for (Map.Entry<T, String> entry : this.map.entrySet()) {
         this.resolveMultiSelectSetting2(entry.getKey(), entry.getValue(), true);
      }

      return this;
   }

   public void setSelectSortType(MultiSelectSetting.SelectSortType selectSortType) {
      this.selectSortType = selectSortType;
   }

   @Override
   public boolean testObject3(Object object) {
      return this.valueClassValue.isInstance(object) ? this.getValue().getOrDefault(object, false) : false;
   }

   public enum SelectSortType {
      Free {
         @Override
         public <T> Map<T, String> sort(Map<T, String> element2name, Map<String, T> name2element) {
            return element2name;
         }
      },
      Small2Big {
         @Override
         public <T> Map<T, String> sort(Map<T, String> element2name, Map<String, T> name2element) {
            return element2name.entrySet()
               .stream()
               .sorted(Map.Entry.comparingByValue(Comparator.comparing(String::length)))
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
         }
      },
      Big2Small {
         @Override
         public <T> Map<T, String> sort(Map<T, String> element2name, Map<String, T> name2element) {
            return element2name.entrySet()
               .stream()
               .sorted(Map.Entry.comparingByValue(Comparator.comparing(String::length).reversed()))
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
         }
      };

      public abstract <T> Map<T, String> sort(Map<T, String> var1, Map<String, T> var2);
   }
}
