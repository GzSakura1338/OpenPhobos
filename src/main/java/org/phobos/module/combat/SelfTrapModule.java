package org.phobos.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.TrapModule;
import org.phobos.core.module.Categories;
import org.phobos.core.module.ModuleListenerUtils;

public class SelfTrapModule extends TrapModule {
   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.handlePlacementModule(this, itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerEntity::equals);
   }

   public SelfTrapModule(PhobosClient phobosClient) {
      super(phobosClient, "SelfTrap", "Traps yourself", Categories.combat, 1);
      ModuleListenerUtils.handleListenerHolder4(this, this::run11);
   }
}
