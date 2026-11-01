package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

public record SetBlockStateEvent(BlockPos pos, BlockState state, WorldChunk chunk) {
}
