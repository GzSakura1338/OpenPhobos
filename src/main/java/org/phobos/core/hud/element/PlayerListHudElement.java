package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.hud.HudElement;

public class PlayerListHudElement extends HudElement {
   public static PlayerListHudElement playerListHudElement;

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
   }

   public static PlayerListHudElement getPlayerListHudElement() {
      return playerListHudElement;
   }

   public PlayerListHudElement() {
      super("Tab", 0.0F, 0.0F, 0.0F, 0.0F);
      playerListHudElement = this;
   }
}
