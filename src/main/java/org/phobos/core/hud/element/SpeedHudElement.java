package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.hud.TextHudElement;

public class SpeedHudElement extends TextHudElement {
   public static final Logger LOGGER = LoggerFactory.getLogger(SpeedHudElement.class);

   public SpeedHudElement() {
      super("Speed");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.player != null) {
         double d = this.phobosClient.getMovementSimulator().getDoubleValue();
         String text = "km/h";
         String text2 = this.describeText2("Speed: ", d + text);
         this.handleDrawContext9(drawContext, text2);
      }
   }
}
