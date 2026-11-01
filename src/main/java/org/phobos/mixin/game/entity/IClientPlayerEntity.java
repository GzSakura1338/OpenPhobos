package org.phobos.mixin.game.entity;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface IClientPlayerEntity {
   @Accessor("lastOnGround")
   void setLastOnGround(boolean var1);

   @Accessor("ticksSinceLastPositionPacketSent")
   void setTicksSinceLastPosition(int var1);
}
