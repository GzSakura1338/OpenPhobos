package org.phobos.mixin.net.interfaces;

import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPositionS2CPacket.class)
public interface IEntityPositionS2SPacket {
   @Mutable
   @Accessor("entityId")
   void setEntityId(int var1);
}
