package org.phobos.module.misc;

import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.module.Module;

public class NoServerPackModule extends Module {
   public boolean flag;
   public int intValue;
   public boolean flag2;

   public NoServerPackModule(PhobosClient phobosClient) {
      super(phobosClient, "NoServerPack", Categories.misc, "Prevents the server from forcing you into using a resource pack");
      this.handleListener(new Listener<Receive<ResourcePackSendS2CPacket>>() {
         public void handleObject2(Receive<ResourcePackSendS2CPacket> receive) {
            NoServerPackModule.this.flag = true;
            NoServerPackModule.this.flag2 = false;
            NoServerPackModule.this.intValue = 0;
            receive.setCancelled(true);
         }
      });
      this.handleListener(new Listener<PlayerTickEvent>() {
         public void handleObject2(PlayerTickEvent playerTickEvent) {
            if (NoServerPackModule.this.flag && MinecraftHolder.mc.player != null) {
               NoServerPackModule.this.intValue++;
               if (NoServerPackModule.this.intValue > Math.random() * 15.0 + 15.0 && !NoServerPackModule.this.flag2) {
                  MinecraftHolder.mc.player.networkHandler.sendPacket(new ResourcePackStatusC2SPacket(MinecraftHolder.mc.player.getUuid(), Status.ACCEPTED));
                  NoServerPackModule.this.flag2 = true;
               }

               if (NoServerPackModule.this.intValue > Math.random() * 20.0 + 40.0 && !NoServerPackModule.this.flag2) {
                  MinecraftHolder.mc.player.networkHandler.sendPacket(new ResourcePackStatusC2SPacket(MinecraftHolder.mc.player.getUuid(), Status.SUCCESSFULLY_LOADED));
                  NoServerPackModule.this.flag = true;
               }
            }
         }
      });
   }
}
