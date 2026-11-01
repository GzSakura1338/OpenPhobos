package org.phobos.mixin.game.world;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientPlayerInteractionManager.class)
public interface IClientPlayerInteractionManager {
   @Invoker("syncSelectedSlot")
   void invokeSyncSelectedSlot();
}
