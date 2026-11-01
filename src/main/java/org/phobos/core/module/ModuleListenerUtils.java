package org.phobos.core.module;

import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.phobos.api.ListenerHolder;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.DisconnectEvent;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public final class ModuleListenerUtils implements MinecraftHolder {
   public static void handleListenerHolder(ListenerHolder listenerHolder, Runnable runnableArg) {
      listenerHolder.getListenerList().add(new Listener<DisconnectEvent2>() {
         public void handleObject2(DisconnectEvent2 disconnectEvent2) {
            runnableArg.run();
         }
      });
   }

   public static void handleModule(Module module) {
      handleListenerHolder4(module, module::run11);
   }

   public static void handleListenerHolder2(ListenerHolder listenerHolder, Runnable runnableArg) {
      listenerHolder.getListenerList().add(new Listener<Receive<PlayerRespawnS2CPacket>>() {
         public void handleObject2(Receive<PlayerRespawnS2CPacket> receive) {
            MinecraftHolder.mc.submit(runnableArg);
         }
      });
   }

   public static void handleListenerHolder3(ListenerHolder listenerHolder, Runnable runnableArg) {
      listenerHolder.getListenerList().add(new Listener<DisconnectEvent>() {
         public void handleObject2(DisconnectEvent disconnectEvent) {
            runnableArg.run();
         }
      });
   }

   public static void handleListenerHolder4(ListenerHolder listenerHolder, Runnable runnableArg) {
      listenerHolder.getListenerList().add(new Listener<DisconnectEvent>() {
         public void handleObject2(DisconnectEvent disconnectEvent) {
            runnableArg.run();
         }
      });
      listenerHolder.getListenerList().add(new Listener<Receive<PlayerRespawnS2CPacket>>() {
         public void handleObject2(Receive<PlayerRespawnS2CPacket> receive) {
            MinecraftHolder.mc.submit(runnableArg);
         }
      });
   }

   public ModuleListenerUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
