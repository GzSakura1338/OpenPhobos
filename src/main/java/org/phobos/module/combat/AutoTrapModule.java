package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.TrapModule;
import org.phobos.core.module.Categories;
import org.phobos.core.entity.EntityUtils;

public class AutoTrapModule extends TrapModule {
   public static AutoTrapModule autoTrapModule;

   public AutoTrapModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTrap", "Traps your enemies", Categories.combat, 2);
      autoTrapModule = this;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.handlePlacementModule(
         this,
         itemSwitcher,
         clientPlayerEntity,
         clientWorld,
         playerEntity -> EntityUtils.testPhobosClient(this.phobosClient, clientPlayerEntity, playerEntity, 8.0)
      );
   }

   public static AutoTrapModule getAutoTrapModule() {
      return autoTrapModule;
   }
}
