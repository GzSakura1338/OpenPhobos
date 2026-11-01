package org.phobos.core.setting;

import lombok.SneakyThrows;
import com.google.gson.JsonElement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.api.ElementCreator;
import org.phobos.api.Named;
import org.phobos.api.TextProvider;
import org.phobos.api.ObjectSource;
import org.phobos.api.Visibility;
import org.phobos.api.MutableValue;
import org.phobos.api.SettingContainer;
import org.phobos.api.JsonSerializable;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.gui.PanelRegistry;
import org.phobos.core.event.events.SettingChangeEvent;
import org.phobos.core.command.Suggestion;

public abstract class Setting<V>
   implements MutableValue<V>,
   Supplier<V>,
   Predicate<V>,
   Named,
   TextProvider,
   JsonSerializable,
   ObjectSource<V>,
   ElementCreator,
   EventSubscriber {
   public Visibility visibility = Visibility.INSTANCE;
   public final String name;
   public boolean flag4;
   public final V object;
   @Nullable
   public String text2;
   public final List<Consumer<V>> consumerList;
   public GuiElement guiElement;
   public boolean flag5;
   public final List<BiConsumer<V, V>> biConsumerList;
   public boolean flag6;
   public SettingContainer settingContainer;
   public boolean flag7 = true;
   public V value;

   public Setting<V> resolveSetting(Setting<Boolean> setting) {
      this.visibility = this.visibility == null ? setting::getValue : this.visibility.resolveVisibility5(setting::getValue);
      return this;
   }

   @SneakyThrows
   public <T> boolean testObject(T object) {
      try {
         this.setValue((V)object);
         return true;
      } catch (Throwable throwable) {
         return false;
      }
   }

   @OverrideOnly
   public Visibility resolveVisibility(V... objects) {
      return Visibility.resolveVisibility3(this, objects);
   }

   @Override
   public boolean test(V object) {
      return this.value == object;
   }

   @OverrideOnly
   @Nullable
   public Suggestion resolveSuggestion(@NotNull String... texts) {
      return null;
   }

   public Setting<V> resolveSetting2(Visibility visibility, boolean flag) {
      this.visibility = flag && this.visibility != null ? this.visibility.resolveVisibility5(visibility) : visibility;
      return this;
   }

   public void run2() {
      this.setValue(this.getObject());
   }

   @Override
   public JsonElement createJsonPrimitive() {
      return null;
   }

   public Class<V> getValueClassValue2() {
      return (Class<V>)this.getObject().getClass();
   }

   public void handleRunnable(Runnable runnable) {
      this.flag5 = true;
      runnable.run();
      this.flag5 = false;
   }

   public Setting<V> resolveSetting3(Visibility visibility) {
      return this.resolveSetting2(visibility, false);
   }

   @Override
   public void setValue(V object) {
      if (this.isFlag4()) {
         this.consumerList.forEach(consumer -> consumer.accept(this.value));
      }

      SettingChangeEvent<V, Setting<V>> settingChangeEvent = this.invoke(new SettingChangeEvent<>(this.settingContainer, this, object));
      if (!settingChangeEvent.isCancelled()) {
         if (!this.flag5 && MenuNavigation.check() && this.guiElement != null) {
            PanelRegistry.getINSTANCE().run();
         }

         V object2 = this.value;
         this.value = object;
         if (this.isFlag4()) {
            this.biConsumerList.forEach(biConsumer -> biConsumer.accept(object2, this.value));
         }

         if (!this.isFlag4()) {
            this.setFlag4(true);
         }
      }
   }

   public <T> Setting<V> resolveSetting4(Setting<T> setting, T object) {
      this.visibility = this.visibility == null
         ? () -> setting.getValue().equals(object)
         : this.visibility.resolveVisibility5(() -> setting.getValue().equals(object));
      return this;
   }

   public <T extends Setting<T>> T resolveSetting5(Class<T> classValue) {
      return (T)this;
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
   }

   @Override
   public V get() {
      return this.value;
   }

   public Setting<V> resolveSetting6(SettingContainer settingContainer) {
      settingContainer.resolveSetting2(this);
      return this;
   }

   public Setting<V> resolveSetting7(String text) {
      this.text2 = text;
      return this;
   }

   public Setting<V> getSetting() {
      this.flag7 = false;
      return this;
   }

   public Setting<V> getSetting2() {
      this.visibility = Visibility.INSTANCE2;
      return this;
   }

   @OverrideOnly
   public boolean testString(@NotNull String... texts) {
      return false;
   }

   public void setValue2(V object) {
      this.value = object;
   }

   public void handleConsumer(Consumer<V> consumer) {
      this.consumerList.add(consumer);
   }

   @SafeVarargs
   public final <T> Setting<V> resolveSetting8(Setting<T> setting, T... objects) {
      Visibility visibility = Visibility.resolveVisibility3(setting, objects);
      this.visibility = this.visibility == null ? visibility : this.visibility.resolveVisibility5(visibility);
      return this;
   }

   public <T extends V> boolean testObject2(T object) {
      return this.value == object;
   }

   public boolean testObject3(Object object) {
      return this.value == object;
   }

   public Setting(String text, @Nullable String text2, V object) {
      this.consumerList = new CopyOnWriteArrayList<>();
      this.biConsumerList = new CopyOnWriteArrayList<>();
      this.flag5 = false;
      this.name = text;
      this.text2 = text2;
      this.object = object;
      this.value = object;
   }

   public Object getNumber22() {
      return this.object;
   }

   public void handleConsumer2(Consumer<V> consumer) {
      this.biConsumerList.add((object, object2) -> consumer.accept(object2));
   }

   public void handleBiConsumer(BiConsumer<V, V> biConsumer) {
      this.biConsumerList.add(biConsumer);
   }

   public SettingContainer getSettingContainer() {
      return this.settingContainer;
   }

   public void setFlag4(boolean flag) {
      this.flag4 = flag;
   }

   public void setFlag6(boolean flag) {
      this.flag6 = flag;
   }

   @Override
   public V getValue() {
      return this.value;
   }

   public void setText3(@Nullable String text) {
      this.text2 = text;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public List<BiConsumer<V, V>> getBiConsumerList() {
      return this.biConsumerList;
   }

   public boolean isFlag5() {
      return this.flag5;
   }

   public void setFlag5(boolean flag) {
      this.flag5 = flag;
   }

   public Visibility getVisibility() {
      return this.visibility;
   }

   @Override
   public V getObject() {
      return this.object;
   }

   public boolean isFlag7() {
      return this.flag7;
   }

   public void setFlag7(boolean flag) {
      this.flag7 = flag;
   }

   public List<Consumer<V>> getConsumerList() {
      return this.consumerList;
   }

   public <T> T resolveObject(Function<V, T> function) {
      return function.apply(this.value);
   }

   public void setVisibility(Visibility visibility) {
      this.visibility = visibility;
   }

   public GuiElement getGuiElement() {
      return this.guiElement;
   }

   public void setGuiElement(GuiElement guiElement) {
      this.guiElement = guiElement;
   }

   @Override
   public String toString() {
      return String.valueOf(this.getValue());
   }

   public void setSettingContainer(SettingContainer settingContainer) {
      this.settingContainer = settingContainer;
   }

   public boolean isFlag4() {
      return this.flag4;
   }

   @SneakyThrows
   public <T> T resolveObject2(Class<T> classValue) {
      return (T)this.value;
   }

   @Nullable
   @Override
   public String getText() {
      return this.text2;
   }

   public boolean isFlag6() {
      return this.flag6;
   }
}
