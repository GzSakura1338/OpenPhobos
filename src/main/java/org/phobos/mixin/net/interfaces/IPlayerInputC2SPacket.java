package org.phobos.mixin.net.interfaces;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInputC2SPacket.class)
public interface IPlayerInputC2SPacket {
   @Mutable
   @Accessor("sideways")
   void setSideways(float var1);

   @Mutable
   @Accessor("sneaking")
   void setSneaking(boolean var1);

   @Mutable
   @Accessor("forward")
   void setForward(float var1);

   @Mutable
   @Accessor("jumping")
   void setJumping(boolean var1);
}
