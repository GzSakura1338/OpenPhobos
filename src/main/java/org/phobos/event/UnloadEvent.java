package org.phobos.event;

import net.minecraft.world.chunk.WorldChunk;

public record UnloadEvent(WorldChunk chunk) {
}
