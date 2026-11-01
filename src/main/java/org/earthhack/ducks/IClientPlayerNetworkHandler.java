package org.earthhack.ducks;

import net.minecraft.network.packet.Packet;

public interface IClientPlayerNetworkHandler {
   void handlePacket(Packet<?> var1);
}
