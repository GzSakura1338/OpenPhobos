package org.phobos.mixin.net;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.OnPacketExceptionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class MixinClientCommonNetworkHandler implements EventSubscriber {
   @Shadow
   public abstract void sendPacket(Packet<?> var1);

   @Inject(
      method = "onPacketException",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientCommonNetworkHandler;strictErrorHandling:Z"),
      cancellable = true
   )
   private void onPacketExceptionHook(Packet<?> packet, Exception exception, CallbackInfo ci) {
      this.invoke(new OnPacketExceptionEvent(), ci);
   }
}
