package org.phobos.core.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.phobos.core.Feature;

public class ScreenManager extends Feature {
   public Screen screen;

   public void run() {
      if (this.screen == null) {
         this.screen = mc.currentScreen;
         mc.currentScreen = null;
      }
   }

   public void run2() {
      if (mc.interactionManager != null && mc.player != null) {
         if (mc.interactionManager.hasRidingInventory()) {
            mc.player.openRidingInventory();
         } else {
            mc.getTutorialManager().onInventoryOpened();
            mc.setScreen(new InventoryScreen(mc.player));
         }
      }
   }

   public void run3() {
      if (this.screen != null) {
         this.screen.close();
         this.screen = null;
      }
   }
}
