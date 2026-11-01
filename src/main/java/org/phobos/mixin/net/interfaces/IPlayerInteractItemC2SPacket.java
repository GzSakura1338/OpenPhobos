package org.phobos.mixin.net.interfaces;

import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInteractItemC2SPacket.class)
public interface IPlayerInteractItemC2SPacket {
   @Mutable
   @Accessor("pitch")
   void setPitch(float var1);

   @Mutable
   @Accessor("yaw")
   void setYaw(float var1);
}
