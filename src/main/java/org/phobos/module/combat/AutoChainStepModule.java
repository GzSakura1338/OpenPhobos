package org.phobos.module.combat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class AutoChainStepModule extends Module {
   public AutoChainStepModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoChainStep", Categories.combat);
      this.handleListener(
         new SafeListener<PlayerTickEvent>() {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                  Block block = itemSwitcher.resolveBlock(Blocks.CHAIN);
                  if (block != null) {
                     ;
                  }
               });
            }
         }
      );
   }
}
