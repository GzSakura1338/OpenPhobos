package org.earthhack.ducks;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.phobos.api.TimeGate;

public interface IInventoryScreen {
   TimeGate.Threaded getOpenTime();

   static IInventoryScreen generic(InventoryScreen screen) {
      return (IInventoryScreen)(Object)screen;
   }
}
