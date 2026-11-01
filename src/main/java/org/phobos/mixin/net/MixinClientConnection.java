package org.phobos.mixin.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.handler.PacketSizeLogger;
import net.minecraft.network.packet.BundlePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.server.ConnectionInfo;
import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.ClientHolder;
import org.phobos.event.AddHandlersEvent;
import org.phobos.event.ConnectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnection implements EventSubscriber {
   private static final Logger LOGGER = LoggerFactory.getLogger(MixinClientConnection.class);

   @Unique
   private void readPacked(
      Packet<?> packet, Function<Packet<?>, CancellableEvent> eventFactory, @Nullable Function<Packet<?>, CancellableEvent> postReceiveEventFactory
   ) {
      if (packet instanceof BundlePacket<?> packed) {
         Iterator iterator = packed.getPackets().iterator();

         while (iterator.hasNext()) {
            Packet<?> unpacked = (Packet<?>)iterator.next();
            CancellableEvent normal = eventFactory.apply(unpacked);
            this.invoke(normal, unpacked.getClass());
            if (normal.isCancelled() && postReceiveEventFactory != null) {
               try {
                  iterator.remove();
                  this.invoke(postReceiveEventFactory.apply(unpacked), unpacked.getClass());
               } catch (UnsupportedOperationException var9) {
               }
            }
         }
      }
   }

   @Unique
   private PacketEvent<?> getPostSend(Packet<?> packet) {
      return new PacketEvent.PostSend<>(packet);
   }

   @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("RETURN"))
   private void channelRead0Hook2(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
      try {
         this.invoke(this.getPostReceive(packet), packet.getClass());
         this.readPacked(packet, this::getPostReceive, null);
      } catch (Exception var5) {
      }
   }

   @Inject(method = "sendImmediately", at = @At("RETURN"))
   private void sendImmediatelyHook2(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
      this.invoke(this.getPostSend(packet), packet.getClass());
   }

   @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
   private void sendImmediatelyHook1(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
      this.invoke(this.getSend(packet), packet.getClass(), ci);
   }

   @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
   private void channelRead0Hook1(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
      try {
         this.invoke(this.getReceive(packet), packet.getClass(), ci);
         this.readPacked(packet, this::getReceive, this::getPostReceive);
      } catch (Exception var5) {
      }
   }

   @Unique
   private PacketEvent<?> getPostReceive(Packet<?> packet) {
      return new PacketEvent.PostReceive<>(packet);
   }

   @Unique
   private PacketEvent<?> getReceive(Packet<?> packet) {
      return new PacketEvent.Receive<>(packet);
   }

   @Inject(method = "addHandlers", at = @At("HEAD"))
   private static void addHandlersHook(ChannelPipeline pipeline, NetworkSide side, boolean local, PacketSizeLogger packetSizeLogger, CallbackInfo ci) {
      if (side.equals(NetworkSide.CLIENTBOUND)) {
         ClientHolder.getEventBus().handleObject2(new AddHandlersEvent(pipeline, local));
      }
   }

   @Unique
   private PacketEvent<?> getSend(Packet<?> packet) {
      return new PacketEvent.Send<>(packet);
   }

   @Inject(
      method = "connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/util/profiler/MultiValueDebugSampleLogImpl;)Lnet/minecraft/network/ClientConnection;",
      at = @At("HEAD")
   )
   private static void connectHook(
      InetSocketAddress address, boolean useEpoll, MultiValueDebugSampleLogImpl packetSizeLog, CallbackInfoReturnable<ClientConnection> cir
   ) {
      new ConnectEvent().resolveObject(resolver -> resolver.resolveConnectEvent3(address.getHostString())).resolveObject(resolver -> {
         try {
            InetSocketAddress real = new InetSocketAddress(InetAddress.getByAddress(resolver.text, address.getAddress().getAddress()), address.getPort());
            ClientHolder.getEventBus().handleObject2(new ConnectionInfo(address.getAddress().getHostName(), real.getAddress().getHostAddress()));
         } catch (Exception var3) {
         }
      });
   }
}
