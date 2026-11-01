package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.text.Text;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.client.NotificationsModule;

public final class DisablerReceiveListener extends ModuleListener.Safe<DisablerModule, PacketEvent.Receive<?>> {
   public DisablerReceiveListener(DisablerModule disablerModule) {
      super(disablerModule, PacketEvent.Receive.class);
   }

   public void handleObject(
      PacketEvent.Receive<?> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      Packet<?> packet = receive.getPacket();
      if (!this.module.flag7) {
         switch ((DisablerModule.Mode)this.module.mode.getValue()) {
            case Watchdog:
               if (!this.module.motion.getValue()) {
                  return;
               }

               if (packet instanceof PlayerPositionLookS2CPacket && this.module.flag11) {
                  this.module.intValue3++;
                  StringBuilder stringBuilder = new StringBuilder();

                  for (int i = 0; i < 20; i++) {
                     stringBuilder.append(i <= this.module.intValue3 ? "▂" : "▁");
                  }

                  float f = Math.round(this.module.intValue3 / 20.0F * 100.0F);
                  stringBuilder.append(" ").append(f).append("%");
                  NotificationsModule.getNotificationsModule().handleText9(Text.literal(stringBuilder.toString()), "WatchdogMotionDisabler");
                  if (this.module.intValue3 >= 20) {
                     NotificationsModule.getNotificationsModule().handleText9(Text.literal("Disabler is now active"), "WatchdogMotionDisabler");
                     this.module.flag11 = false;
                     this.module.intValue3 = 0;
                  }
               }

               if (packet instanceof PlayerRespawnS2CPacket) {
                  this.module.intValue3 = 0;
                  this.module.flag11 = false;
                  this.module.flag9 = true;
               }
               break;
            case Sentinel:
               if (packet instanceof PlayerRespawnS2CPacket) {
                  this.module.intValue = 0;
               }
               break;
            case Grim_Full:
               if (packet instanceof CommonPingS2CPacket && this.module.intValue5 != -1) {
                  this.module.intValue5--;
                  if (this.module.intValue5 <= 0) {
                     this.module.intValue5 = -1;
                     this.module.flag6 = false;
                  }
               }
               break;
            case Vanilla:
               if (packet instanceof PlayerPositionLookS2CPacket) {
                  this.module.flag2 = true;
               }
         }
      }
   }
}
