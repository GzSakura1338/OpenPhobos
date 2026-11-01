package org.phobos.module.player;

import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.InteractEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;

public class NoInteractModule extends Module {
   public final Setting<Set<Block>> selection = this.resolveSetting2(new BlockSetSetting("Selection"));

   public NoInteractModule(PhobosClient phobosClient) {
      super(phobosClient, "NoInteract", Categories.player, "Cancels interactions with specified blocks");
      this.handleListener(
         new SafeListener<InteractEvent>() {
            public void handleObject(
               InteractEvent interactEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (interactEvent.getBlockHitResult() != null) {
                  BlockPos blockPos = interactEvent.getBlockHitResult().getBlockPos();
                  if (blockPos != null) {
                     Block block = clientWorld.getBlockState(blockPos).getBlock();
                     if (NoInteractModule.this.selection.getValue().contains(block)) {
                        interactEvent.setCancelled(true);
                     }
                  }
               }
            }
         }
      );
   }
}
