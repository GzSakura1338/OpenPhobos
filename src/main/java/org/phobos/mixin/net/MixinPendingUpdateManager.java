package org.phobos.mixin.net;

import net.minecraft.client.network.PendingUpdateManager;
import org.earthhack.ducks.IPendingUpdateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PendingUpdateManager.class)
public abstract class MixinPendingUpdateManager implements IPendingUpdateManager {
   @Shadow
   private int sequence;

   @Override
   public void decrementSequence() {
      this.sequence--;
   }
}
