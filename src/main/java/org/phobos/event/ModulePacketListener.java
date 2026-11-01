package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.Module;

public abstract class ModulePacketListener<M extends Module> extends ModuleListener.Safe<M, PacketEvent<?>> {
   public ModulePacketListener(M module) {
      super(module, PacketEvent.class);
   }

   public void handleObject(
      PacketEvent<?> packetEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (packetEvent instanceof PacketEvent.Receive<?> receive) {
         this.handleReceive(receive, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
      } else {
         if (packetEvent instanceof PacketEvent.Send<?> send) {
            this.handleSend(send, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
         }
      }
   }

   public abstract void handleSend(PacketEvent.Send<?> var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);

   public abstract void handleReceive(PacketEvent.Receive<?> var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);
}
