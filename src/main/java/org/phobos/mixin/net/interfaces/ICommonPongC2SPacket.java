package org.phobos.mixin.net.interfaces;

import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommonPongC2SPacket.class)
public interface ICommonPongC2SPacket {
   @Mutable
   @Accessor("parameter")
   void setParameter(int var1);
}
