package org.phobos.event;

import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.module.misc.SpooferModule;

public final class SpooferSendCustomPayloadListener extends ModuleListener<SpooferModule, PacketEvent.Send<CustomPayloadC2SPacket>> {
   public SpooferSendCustomPayloadListener(SpooferModule spooferModule) {
      super(spooferModule, PacketEvent.Send.class, CustomPayloadC2SPacket.class);
   }

   public void handleObject2(PacketEvent.Send<CustomPayloadC2SPacket> send) {
      if (mc.player != null) {
         CustomPayload customPayload = ((CustomPayloadC2SPacket)send.getPacket()).payload();
         Identifier identifier = customPayload.getId().id();
         if (identifier.getPath().equals("brand")) {
            send.setCancelled(true);
            SkyboxTextureUtils.handlePacket(new CustomPayloadC2SPacket(new BrandCustomPayload("vanilla")));
         } else {
            if (identifier.getNamespace().equals("fabric") && !mc.isInSingleplayer()) {
               send.setCancelled(true);
            }
         }
      }
   }
}
