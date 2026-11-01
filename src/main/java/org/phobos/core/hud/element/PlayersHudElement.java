package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.hud.HudElement;

public class PlayersHudElement extends HudElement {
   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
   }

   public PlayersHudElement() {
      super("Players", 0.0F, 0.0F, 0.0F, 10.0F);
   }
}
