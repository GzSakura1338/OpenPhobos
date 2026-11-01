package org.phobos.event;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.text.Text;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AnnouncerModule;
import org.phobos.module.client.NotificationsModule;

public final class AnnouncerReceiveListener extends ModuleListener.Safe<AnnouncerModule, PacketEvent.Receive<?>> {
   public AnnouncerReceiveListener(AnnouncerModule announcerModule) {
      super(announcerModule, PacketEvent.Receive.class);
   }

   public void handleObject(
      PacketEvent.Receive<?> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch (receive.getPacket()) {
         case PlayerListS2CPacket playerListS2CPacket:
            if (!this.module.join.getValue()) {
               return;
            }

            if (playerListS2CPacket.getActions().isEmpty()) {
               return;
            }

            Action action = (Action)playerListS2CPacket.getActions().stream().findFirst().orElse(null);
            if (action != Action.ADD_PLAYER) {
               return;
            }

            if (playerListS2CPacket.getEntries().isEmpty()) {
               return;
            }

            Entry entry = (Entry)playerListS2CPacket.getEntries().getFirst();
            if (entry == null) {
               return;
            }

            GameProfile gameProfile = entry.profile();
            if (gameProfile == null) {
               return;
            }

            if (!this.module.normal.testNumber(this.module.delay.getValue())) {
               return;
            }

            NotificationsModule.getNotificationsModule()
               .handleTriConsumer(
                  (color, color2, color3) -> this.module
                     .handleText(
                        "Welcome %s".formatted(gameProfile.getName()),
                        Text.literal("Welcome ").withColor(color3.hashCode()).append(Text.literal(gameProfile.getName()).withColor(color2.hashCode()))
                     )
               );
            break;
         case PlayerRemoveS2CPacket playerRemoveS2CPacket:
            if (this.module.leave.getValue()) {
               return;
            }

            if (playerRemoveS2CPacket.profileIds().isEmpty()) {
               return;
            }

            UUID uUID = (UUID)playerRemoveS2CPacket.profileIds().getFirst();
            if (uUID == null) {
               return;
            }

            PlayerListEntry playerListEntry = clientPlayerEntity.networkHandler.getPlayerListEntry(uUID);
            if (playerListEntry == null) {
               return;
            }

            GameProfile gameProfile2 = playerListEntry.getProfile();
            if (gameProfile2 == null) {
               return;
            }

            if (!this.module.normal.testNumber(this.module.delay.getValue())) {
               return;
            }

            NotificationsModule.getNotificationsModule()
               .handleTriConsumer(
                  (color, color2, color3) -> this.module
                     .handleText(
                        "Goodbye %s".formatted(gameProfile2.getName()),
                        Text.literal("Goodbye ").withColor(color3.hashCode()).append(Text.literal(gameProfile2.getName()).withColor(color2.hashCode()))
                     )
               );
            return;
         default:
      }
   }
}
