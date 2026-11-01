package org.phobos.module.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.phobos.api.TextProvider;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.config.BuildInfo;
import org.phobos.core.user.UserSession;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.module.Module;

public class RPCModule extends Module {
   public final RPCModule.Handler handler;
   public static String text = "";
   public final Setting<Boolean> actions;
   public final Setting<RPCModule.RPCLine> info = this.resolveSetting2(new EnumSetting<>("Info", RPCModule.RPCLine.None));

   public RPCModule(PhobosClient phobosClient) {
      super(phobosClient, "RPC", Categories.client, "Discord status");
      this.actions = this.resolveSetting2(new BooleanSetting("Actions").resolveSetting7("Enables ask to join game button"));
      this.handler = new RPCModule.Handler(this);
      if (UserSession.userSession.getUserProfile().getPermission().isBeta()) {
         text = "Beta ";
      }
   }

   public Setting<RPCModule.RPCLine> getInfo() {
      return this.info;
   }

   @Override
   public void run4() {
      this.handler.stop();
   }

   @Override
   public void run3() {
      this.handler.start();
   }

   public RPCModule.Handler getHandler() {
      return this.handler;
   }

   public Setting<Boolean> getActions() {
      return this.actions;
   }

   private static class Handler implements MinecraftHolder {
      public static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
      public static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;
      public final Object object = new Object[0];
      public Thread thread;
      public final RPCModule rPCModule;
      private static Object[] phobos$g1806d6398;

      public void start() {
         synchronized (this.object) {
            if (this.thread != null) {
               this.thread.interrupt();
            }

            DiscordEventHandlers discordEventHandlers = new DiscordEventHandlers();
            discordRPC.Discord_Initialize("1254696672911691839", discordEventHandlers, true, "");
            discordRPC.Discord_Register("1254696672911691839", null);
            discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
            Normal normal = new Normal();
            Runnable runnable = () -> this.handleNormal(normal);
            this.thread = new Thread(runnable, "Discord-RPC-Thread-1");
            normal.run();
            this.thread.setDaemon(true);
            this.thread.start();
         }
      }
      public void stop() {
         synchronized (this.object) {
            discordRPC.Discord_Shutdown();
            if (this.thread != null && !this.thread.isInterrupted()) {
               this.thread.interrupt();
               this.thread = null;
            }
         }
      }

      public String describe() {
         String var10000;
         switch ((RPCModule.RPCLine)this.rPCModule.getInfo().getValue()) {
            case Latency:
               var10000 = mc.world == null
                  ? "Main menu"
                  : (mc.isIntegratedServerRunning() ? "Singleplayer" : (mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : ""));
               break;
            case Server:
               var10000 = mc.player == null ? "" : mc.player.getNameForScoreboard();
               break;
            case Username:
               var10000 = mc.player != null && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) != null
                  ? mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency() + "ms"
                  : "";
               break;
            case None:
               var10000 = "";
               break;
            default:
               throw new MatchException(null, null);
         }

         return var10000;
      }

      public Handler(RPCModule rPCModule) {
         this.rPCModule = rPCModule;
      }

      public void handleNormal(Normal normal) {
         do {
            try {
               Thread.sleep(2000L);
            } catch (InterruptedException interruptedException) {
               Thread.currentThread().interrupt();
            }

            discordRPC.Discord_RunCallbacks();
            if (normal.testLong(1000L)) {
               discordRichPresence.largeImageText = "Phobos.eu";
               discordRichPresence.largeImageKey = "b";
               discordRichPresence.smallImageKey = UserSession.userSession.getUserProfile().getText();
               discordRichPresence.smallImageText = UserSession.userSession.getText();
               discordRichPresence.details = "Phobos " + BuildInfo.getINSTANCE().describe3();
               discordRichPresence.state = this.describe();
               discordRPC.Discord_UpdatePresence(discordRichPresence);
            }
         } while (!Thread.currentThread().isInterrupted());
      }
   }

   private enum RPCLine implements TextProvider {
      Server("Displays current server ip"),
      Username("Displays current username"),
      Latency("Displays current ping to the server"),
      None("No additional information");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      RPCLine(final String description) {
         this.text = description;
      }
   }
}
