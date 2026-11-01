package org.phobos.mixin.game.world;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.ClientEntityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientWorld.class)
public interface IClientWorld {
   @Accessor("networkHandler")
   ClientPlayNetworkHandler getConnection();

   @Accessor("entityManager")
   ClientEntityManager<Entity> getEntityManager();

   @Accessor("pendingUpdateManager")
   PendingUpdateManager getPendingUpdateManager();
}
