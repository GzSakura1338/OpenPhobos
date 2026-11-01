package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.hud.FpsCounter;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.TextHudElement;

public class FpsHudElement extends TextHudElement {
   public static final Logger LOGGER = LoggerFactory.getLogger(FpsHudElement.class);
   public final Setting<Boolean> fast = this.resolveSetting2(new BooleanSetting("Fast", true));

   public FpsHudElement() {
      super("FPS");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      int i3 = this.fast.getValue() ? FpsCounter.value() : mc.getCurrentFps();
      String text = this.describeText2("FPS: ", String.valueOf(i3));
      this.handleDrawContext9(drawContext, text);
   }
}
