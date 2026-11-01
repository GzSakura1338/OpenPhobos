package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.hud.TextHudElement;

public class ServerBrandHudElement extends TextHudElement {
   public ServerBrandHudElement() {
      super("ServerBrand");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = mc.getServer() != null
         ? mc.getServer().getVersion()
         : (mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().version.getString() : null);
      if (text != null) {
         String text2 = this.describeText2("ServerBrand: ", text);
         this.handleDrawContext9(drawContext, text2);
      }
   }
}
