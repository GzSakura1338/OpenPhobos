package org.phobos.mixin.game.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.ChunkWorkerHolder;
import org.phobos.core.task.TaskQueue;
import org.phobos.event.SetBlockStateEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk implements ChunkWorkerHolder, EventSubscriber {
   @Unique
   private final TaskQueue holeChunkWorker = new TaskQueue();
   @Final
   @Shadow
   World world;
   @Unique
   private final TaskQueue graphChunkWorker = new TaskQueue();

   @Override
   public TaskQueue getHoleChunkWorker() {
      return this.holeChunkWorker;
   }

   @Override
   public TaskQueue getGraphChunkWorker() {
      return this.graphChunkWorker;
   }

   @Inject(method = "setBlockState", at = @At("RETURN"))
   private void setBlockStateHook(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
      if (this.world.isClient) {
         this.invoke(new SetBlockStateEvent(pos, state, WorldChunk.class.cast(this)));
      }
   }
}
