package org.phobos.mixin.net.interfaces;

import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkDeltaUpdateS2CPacket.class)
public interface IChunkDeltaUpdateS2CPacket {
   @Accessor("positions")
   short[] getPositions();
}
