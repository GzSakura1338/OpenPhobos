package org.phobos.core.net;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.thread.ThreadExecutor;
import org.phobos.event.Listener.AnyType;
import org.phobos.core.Feature;
import org.phobos.core.event.events.PacketEvent;

public abstract class PacketListenerBase<P extends Packet<?>> extends Feature {
   public final ThreadLocal<Packet<?>> packetThreadLocal;
   public final ThreadLocal<List<Runnable>> listThreadLocal = new ThreadLocal<>();

   public void handlePostReceive(PacketEvent.PostReceive<P> postReceive) {
      if (postReceive.getPacket() == this.packetThreadLocal.get() && this.listThreadLocal.get() != null) {
         this.listThreadLocal.get().forEach(Runnable::run);
      }

      this.packetThreadLocal.remove();
      this.listThreadLocal.remove();
   }

   public PacketListenerBase(Function<Class<?>, Class<P>> function) {
      this.packetThreadLocal = new ThreadLocal<>();
      Class<P> classValue = function.apply(this.getClass());
      this.handleListener(new AnyType<PacketEvent.Receive<P>>(PacketEvent.Receive.class, classValue) {
         public void handleObject2(PacketEvent.Receive<P> receive) {
            PacketListenerBase.this.packetThreadLocal.set(receive.getPacket());
            PacketListenerBase.this.handleReceive(receive);
         }
      });
      this.handleListener(new AnyType<PacketEvent.PostReceive<P>>(PacketEvent.PostReceive.class, classValue) {
         public void handleObject2(PacketEvent.PostReceive<P> postReceive) {
            PacketListenerBase.this.handlePostReceive(postReceive);
         }
      });
   }

   public PacketListenerBase(Class<P> classValue) {
      this(classValue2 -> classValue);
   }

   public abstract void handleReceive(PacketEvent.Receive<P> var1);

   public void handleThreadExecutor(ThreadExecutor<?> threadExecutor, Runnable runnable) {
      this.handleRunnable(() -> threadExecutor.submit(runnable));
   }

   public void handleRunnable(Runnable runnable) {
      if (this.listThreadLocal.get() == null) {
         this.listThreadLocal.set(new ArrayList<>());
      }

      this.listThreadLocal.get().add(runnable);
   }

   public PacketListenerBase() {
      this(classValue -> TypeRecordUtils.<P>resolveTypeRecord(classValue).type());
   }
}
