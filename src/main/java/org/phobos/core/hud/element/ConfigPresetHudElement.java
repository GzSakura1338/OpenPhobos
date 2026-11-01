package org.phobos.core.hud.element;

import java.util.Objects;
import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.event.Listener;
import org.phobos.core.config.NamedHandle;
import org.phobos.core.event.events.SettingChangeEvent;
import org.phobos.core.hud.TextHudElement;

public class ConfigPresetHudElement extends TextHudElement {
   public final MutableObject<String> textMutableObject = new MutableObject();

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = Objects.requireNonNullElse((String)this.textMutableObject.getValue(), "Custom");
      String text2 = this.describeText2("Preset: ", text);
      this.handleDrawContext9(drawContext, text2);
   }

   public ConfigPresetHudElement() {
      super("Config");
      this.handleListener(new Listener<NamedHandle>() {
         public void handleObject2(NamedHandle namedHandle) {
            ConfigPresetHudElement.this.textMutableObject.setValue(namedHandle.getName());
         }
      });
      this.handleListener(new Listener<SettingChangeEvent<?, ?>>() {
         public void handleObject2(SettingChangeEvent<?, ?> settingChangeEvent) {
            ConfigPresetHudElement.this.textMutableObject.setValue(null);
         }
      });
   }
}
