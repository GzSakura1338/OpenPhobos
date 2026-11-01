package org.phobos.module.movement;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.module.Module;

public class SafeWalkModule extends Module {
   public SafeWalkModule(PhobosClient phobosClient) {
      super(phobosClient, "SafeWalk", Categories.movement, "Sneak without sneak");
   }

   @Override
   public void run4() {
      SwitchOverride.run3();
   }

   @Override
   public void run3() {
      SwitchOverride.handleFlag(true);
   }
}
