package org.phobos.mixin.net;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.earthhack.ducks.IPlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerMoveC2SPacket.class)
public abstract class MixinPlayerMoveC2SPacket implements IPlayerMoveC2SPacket {
   @Unique
   private boolean skipping;

   @Override
   public boolean isSkipping() {
      return this.skipping;
   }

   @Override
   public void setSkipping() {
      this.skipping = true;
   }
}
