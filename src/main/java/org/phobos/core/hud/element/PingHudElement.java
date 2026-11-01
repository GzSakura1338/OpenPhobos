package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.util.holder.IntHolder;
import org.phobos.core.hud.TextHudElement;

public class PingHudElement extends TextHudElement implements EventSubscriber {
   public PingHudElement() {
      super("Ping");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.getNetworkHandler() != null && mc.player != null) {
         PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
         if (playerListEntry != null) {
            IntHolder intHolder = this.invoke(new IntHolder(playerListEntry.getLatency()));
            String text = this.describeText2("Ping: ", String.valueOf(intHolder.getIntValue()));
            this.handleDrawContext9(drawContext, text);
         }
      }
   }
}
