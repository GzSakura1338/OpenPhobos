package org.phobos.core.server;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.listener.ServerHandshakePacketListener;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

public class ServerHandshakeListener implements ServerHandshakePacketListener {
   public final ClientConnection clientConnection;

   public ServerHandshakeListener(ClientConnection clientConnection) {
      this.clientConnection = clientConnection;
   }

   public boolean isConnectionOpen() {
      return false;
   }

   public void onDisconnected(DisconnectionInfo info) {
   }

   public void onHandshake(HandshakeC2SPacket packet) {
   }
}
