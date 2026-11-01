package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AutoLoginModule;
import org.phobos.module.misc.AutoLoginModule;
import org.phobos.module.misc.AutoLoginModule;
import org.phobos.module.misc.AutoLoginModule;

public final class AutoLoginReceiveListener extends ModuleListener<AutoLoginModule, PacketEvent.Receive<?>> {
   public AutoLoginReceiveListener(AutoLoginModule autoLoginModule) {
      super(autoLoginModule, PacketEvent.Receive.class);
   }

   public void run() {
      ClientPlayerEntity clientPlayerEntity = mc.player;
      if (clientPlayerEntity != null) {
         switch ((AutoLoginModule.Mode)this.module.mode.getValue()) {
            case Single:
               clientPlayerEntity.networkHandler.sendChatCommand(this.module.command.getValue().replace("%password%", this.module.password.getValue()));
               break;
            case Multi:
               String text = this.phobosClient.getUserTagManager().getName();
               if (text == null) {
                  return;
               }

               AutoLoginModule.LoginMap loginMap = this.module.loginMap;
               AutoLoginModule.Login login = loginMap.resolveLogin(text);
               if (login != null) {
                  clientPlayerEntity.networkHandler.sendChatCommand(this.module.command.getValue().replace("%password%", login.getText()));
               }
         }
      }
   }

   public void handleObject2(PacketEvent.Receive<?> receive) {
      if (!this.module.normal.testNumber(this.module.timeout.getValue())) {
         switch (receive.getPacket()) {
            case ChatMessageS2CPacket chatMessageS2CPacket:
               String text = chatMessageS2CPacket.body().content();
               if (text.contains(this.module.trigger.getValue())) {
                  this.run();
               }
               break;
            case ProfilelessChatMessageS2CPacket profilelessChatMessageS2CPacket:
               String text2 = profilelessChatMessageS2CPacket.message().getString();
               if (text2.contains(this.module.trigger.getValue())) {
                  this.run();
               }
               break;
            case GameMessageS2CPacket gameMessageS2CPacket:
               String text3 = gameMessageS2CPacket.content().getString();
               if (text3.contains(this.module.trigger.getValue())) {
                  this.run();
               }
               break;
            default:
         }
      }
   }
}
