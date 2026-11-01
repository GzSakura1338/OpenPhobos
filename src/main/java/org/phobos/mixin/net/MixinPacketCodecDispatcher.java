package org.phobos.mixin.net;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.handler.PacketCodecDispatcher;
import net.minecraft.network.handler.PacketCodecDispatcher.PacketType;
import org.phobos.core.net.PacketTypeUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketCodecDispatcher.class)
public class MixinPacketCodecDispatcher<B extends ByteBuf, V, T> {
   @Inject(method = "<init>", at = @At("TAIL"))
   private void initHook(Function<V, ? extends T> packetIdGetter, List<PacketType<B, V, T>> packetTypes, Object2IntMap<T> typeToIndex, CallbackInfo ci) {
      PacketTypeUtils.setObject2IntMap(typeToIndex);
   }
}
