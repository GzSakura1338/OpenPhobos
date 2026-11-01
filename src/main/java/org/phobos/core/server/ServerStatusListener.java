package org.phobos.core.server;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.listener.ServerQueryPacketListener;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;
import net.minecraft.text.Text;
import org.phobos.module.client.PingBypassModule;

public class ServerStatusListener implements ServerQueryPacketListener {
   public boolean flag;
   public static final Text text = Text.literal("Status request has been handled");
   public final ClientConnection clientConnection;

   public ServerStatusListener(ClientConnection clientConnection) {
      this.clientConnection = clientConnection;
   }

   public void onDisconnected(DisconnectionInfo info) {
   }

   public boolean isConnectionOpen() {
      return false;
   }

   public void onQueryPing(QueryPingC2SPacket packet) {
   }

   public void onRequest(QueryRequestC2SPacket packet) {
      if (this.flag) {
         this.clientConnection.disconnect(text);
      } else {
         this.flag = true;
         this.clientConnection.send(new QueryResponseS2CPacket(PingBypassModule.pingBypassStatus.createServerMetadata()));
      }
   }
}
