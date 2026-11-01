package org.phobos.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.JesusTickListener;
import org.phobos.event.JesusComputeNextListener;
import org.phobos.event.JesusJumpListener;
import org.phobos.module.Module;

public class JesusModule extends Module {
   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return BlockUtils.testClientWorld(
         clientWorld, clientPlayerEntity, (blockPos, blockState) -> !clientWorld.getBlockState(blockPos.down()).getFluidState().isEmpty()
      );
   }

   public JesusModule(PhobosClient phobosClient) {
      super(phobosClient, "Jesus", Categories.movement, "Allows you to walk on water");
      this.handleListener(new JesusComputeNextListener(this));
      this.handleListener(new JesusJumpListener(this));
      this.handleListener(new JesusTickListener(this));
      ModuleGroups.getINSTANCE().handleText("legacy", this);
   }
}
