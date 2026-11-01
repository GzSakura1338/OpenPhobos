package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.hud.TextHudElement;

public class AntiCheatHudElement extends TextHudElement {
   public AntiCheatHudElement() {
      super("AntiCheat");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = this.phobosClient.getServerAntiCheatDetector().getText();
      String text2 = this.describeText2("AntiCheat: ", text);
      this.handleDrawContext9(drawContext, text2);
   }
}
