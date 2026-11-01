package org.phobos.mixin.net.interfaces;

import java.util.UUID;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LoginHelloC2SPacket.class)
public interface ILoginHelloC2SPacket {
   @Mutable
   @Accessor("profileId")
   void setProfileId(UUID var1);

   @Mutable
   @Accessor("name")
   void setName(String var1);
}
