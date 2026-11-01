package org.phobos.module.misc;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.phobos.api.TimeGate;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.AutoReconnectDisconnectedScreenStateListener;
import org.phobos.event.AutoReconnectTickListener;
import org.phobos.mixin.game.ui.IDisconnectedScreen;
import org.phobos.module.Module;

public class AutoReconnectModule extends Module {
   public boolean flag;
   public long longValue;
   public final TimeGate.Normal normal;
   public final Setting<Long> delay = this.resolveSetting2(new LongNumberSetting("Delay", 5000L, 1000L, 60000L));
   public boolean flag2;
   public ServerInfo serverInfo;

   public void handleScreen(Screen screen) {
      if (this.serverInfo != null) {
         IDisconnectedScreen iDisconnectedScreen = (IDisconnectedScreen)(Object)screen;
         ConnectScreen.connect(iDisconnectedScreen.getParent(), mc, ServerAddress.parse(this.serverInfo.address), this.serverInfo, false, null);
      } else {
         this.flag2 = true;
         this.normal.run();
      }
   }

   @Override
   public void run6() {
      ServerInfo serverInfo = mc.getCurrentServerEntry();
      if (serverInfo != null) {
         this.serverInfo = serverInfo;
      }
   }

   public AutoReconnectModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoReconnect", Categories.misc, "Reconnects you to the server");
      this.normal = new TimeGate.Normal();
      this.handleListener(new AutoReconnectDisconnectedScreenStateListener(this));
      this.handleListener(new AutoReconnectTickListener(this));
   }

   public void handleDisconnectedScreen(DisconnectedScreen disconnectedScreen) {
      if (!this.flag) {
         this.normal.run();
      }

      if (this.flag2) {
         if (this.normal.testLong(3000L)) {
            mc.setScreen(new TitleScreen());
         }
      } else if (this.normal.testLong(this.delay.getValue() * 1000L) && this.flag) {
         this.handleScreen(disconnectedScreen);
      }
   }
}
