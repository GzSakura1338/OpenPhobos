package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.hud.TextHudElement;

public class CustomTextHudElement extends TextHudElement {
   public final Setting<String> content = this.resolveSetting2(new StringSetting("Content", "LIVE WEBCAM"));

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      this.handleDrawContext9(drawContext, this.content.getValue());
   }

   public CustomTextHudElement() {
      super("Text");
   }
}
