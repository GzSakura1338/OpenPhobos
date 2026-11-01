package org.phobos.mixin.game.render.light;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkToNibbleArrayMap;
import net.minecraft.world.chunk.light.LightStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LightStorage.class)
public abstract class MixinLightStorage<M extends ChunkToNibbleArrayMap<M>> {
   @Shadow
   @Final
   protected LongSet dirtySections;
   @Shadow
   @Final
   protected LongSet notifySections;
   @Shadow
   @Final
   protected M storage;

   @Shadow
   @Nullable
   protected abstract ChunkNibbleArray getLightSection(long var1, boolean var3);

   @Overwrite
   @SuppressWarnings("overwrite")
   public void set(long blockPos, int value) {
      long l = ChunkSectionPos.fromBlockPos(blockPos);
      ChunkNibbleArray chunkNibbleArray;
      if (this.dirtySections.add(l)) {
         chunkNibbleArray = this.storage.replaceWithCopy(l);
      } else {
         chunkNibbleArray = this.getLightSection(l, true);
      }

      if (chunkNibbleArray != null) {
         chunkNibbleArray.set(
            ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)),
            ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)),
            ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos)),
            value
         );
         LongSet var10001 = this.notifySections;
         ChunkSectionPos.forEachChunkSectionAround(blockPos, var10001::add);
      }
   }
}
