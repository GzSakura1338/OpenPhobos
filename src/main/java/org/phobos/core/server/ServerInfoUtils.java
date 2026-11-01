package org.phobos.core.server;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.client.resource.language.I18n;
import org.phobos.api.MinecraftHolder;

public final class ServerInfoUtils implements MinecraftHolder {
   public static void handleText(String text) {
      ServerInfo serverInfo = createServerInfo(text);
      handleServerInfo(serverInfo);
   }

   public static ServerInfo createServerInfo(String text) {
      return new ServerInfo(I18n.translate("selectServer.defaultName", new Object[0]), text, ServerType.OTHER);
   }

   public ServerInfoUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleServerInfo(ServerInfo serverInfo) {
      ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), mc, ServerAddress.parse(serverInfo.address), serverInfo, false, null);
   }
}
