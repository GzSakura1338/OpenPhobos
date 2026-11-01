package org.phobos.core.setting;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.BuilderHolder;
import org.phobos.core.gui.ElementFactory;
import org.phobos.core.gui.GuiElement;
import org.phobos.core.gui.GuiManager;
import org.phobos.core.gui.GuiPanel;
import org.phobos.core.gui.ModuleElement;

public class ProvidedSetting<T> extends Setting<T> implements BuilderHolder<T, ProvidedSetting<T>> {
   public transient SettingBuilder<T, ProvidedSetting<T>> settingBuilder;
   public transient ElementFactory<T, ProvidedSetting<T>, ?> elementFactory;
   public boolean flag = false;

   public ProvidedSetting<T> resolveProvidedSetting(SettingBuilder<T, ProvidedSetting<T>> settingBuilder) {
      this.settingBuilder = settingBuilder;
      return this;
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public GuiElement createEnumElement(GuiManager guiManager, GuiPanel guiPanel, String text, ModuleElement moduleElement) {
      throw new UnsupportedOperationException();
   }

   public Setting<T> getSetting3() {
      Preconditions.checkState(this.flag, "Setting provider is not indexed");
      return this.settingBuilder.getObject();
   }

   public ElementFactory<T, ProvidedSetting<T>, ?> getElementFactory() {
      return this.elementFactory;
   }

   public void setElementFactory(ElementFactory<T, ProvidedSetting<T>, ?> elementFactory) {
      this.elementFactory = elementFactory;
   }

   public ProvidedSetting(@NotNull String text, T object) {
      super(text, "", object);
   }

   public ProvidedSetting<T> getProvidedSetting() {
      Preconditions.checkNotNull(this.settingBuilder, "SettingBuilder is missing!");
      Preconditions.checkNotNull(this.elementFactory, "ElementFactory is missing!");
      this.setFlag(true);
      return this;
   }

   public ProvidedSetting<T> resolveSetting7(String text) {
      this.setText3(text);
      return this;
   }

   public ProvidedSetting<T> resolveProvidedSetting2(ElementFactory<T, ProvidedSetting<T>, ?> elementFactory) {
      this.elementFactory = elementFactory;
      return this;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public SettingBuilder<T, ProvidedSetting<T>> getSettingBuilder() {
      return this.settingBuilder;
   }

   public void setSettingBuilder(SettingBuilder<T, ProvidedSetting<T>> settingBuilder) {
      this.settingBuilder = settingBuilder;
   }
}
