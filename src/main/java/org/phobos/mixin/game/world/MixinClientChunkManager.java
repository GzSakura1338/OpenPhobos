package org.phobos.mixin.game.world;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.UnloadEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientChunkManager.class)
public abstract class MixinClientChunkManager implements EventSubscriber {
   @Inject(
      method = "unload",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;compareAndSet(ILnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)Lnet/minecraft/world/chunk/WorldChunk;",
         shift = Shift.AFTER
      ),
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void unloadHook(ChunkPos chunkPos, CallbackInfo ci, int index, WorldChunk worldChunk) {
      this.invoke(new UnloadEvent(worldChunk));
   }
}
