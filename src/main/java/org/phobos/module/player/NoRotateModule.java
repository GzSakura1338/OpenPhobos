package org.phobos.module.player;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import org.phobos.api.TextProvider;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.mixin.net.interfaces.IPlayerPositionLookS2CPacket;
import org.phobos.module.Module;

public class NoRotateModule extends Module {
   public final Setting<NoRotateModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", NoRotateModule.Mode.Cancel));

   @Override
   public String nothing() {
      return String.valueOf(this.mode.getValue());
   }

   public NoRotateModule(PhobosClient phobosClient) {
      super(phobosClient, "NoRotate", Categories.player, "Disallows the server to rotate you");
      this.handleListener(
         new SafeListener<Receive<PlayerPositionLookS2CPacket>>() {
            public void handleObject(
               Receive<PlayerPositionLookS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!(mc.currentScreen instanceof DownloadingTerrainScreen)) {
                  IPlayerPositionLookS2CPacket iPlayerPositionLookS2CPacket = (IPlayerPositionLookS2CPacket)(Object)receive.getPacket();
                  switch ((NoRotateModule.Mode)NoRotateModule.this.mode.getValue()) {
                     case Cancel:
                        float f = ((PlayerPositionLookS2CPacket)receive.getPacket()).getYaw();
                        float f2 = ((PlayerPositionLookS2CPacket)receive.getPacket()).getPitch();
                        iPlayerPositionLookS2CPacket.setYaw(clientPlayerEntity.getYaw());
                        iPlayerPositionLookS2CPacket.setPitch(clientPlayerEntity.getPitch());
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.Y_ROT);
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.X_ROT);
                        phobosClient.getRotationManager().setFloatValue2(f);
                        phobosClient.getRotationManager().setFloatValue7(f2);
                        break;
                     case Silent:
                        iPlayerPositionLookS2CPacket.setYaw(clientPlayerEntity.getYaw());
                        iPlayerPositionLookS2CPacket.setPitch(clientPlayerEntity.getPitch());
                        break;
                     case Flags:
                        iPlayerPositionLookS2CPacket.setYaw(clientPlayerEntity.getYaw());
                        iPlayerPositionLookS2CPacket.setPitch(clientPlayerEntity.getPitch());
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.Y_ROT);
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.X_ROT);
                        break;
                     case Grim:
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.Y_ROT);
                        ((PlayerPositionLookS2CPacket)receive.getPacket()).getFlags().remove(PositionFlag.X_ROT);
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<PostReceive<PlayerPositionLookS2CPacket>>() {
            public void handleObject(
               PostReceive<PlayerPositionLookS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!(mc.currentScreen instanceof DownloadingTerrainScreen)) {
                  if (NoRotateModule.this.mode.testObject2(NoRotateModule.Mode.Grim)) {
                     float f = phobosClient.getRotationManager().getFloatValue4();
                     float f2 = phobosClient.getRotationManager().getFloatValue5();
                     phobosClient.getRotationManager().handleClientPlayerEntity4(clientPlayerEntity, f, f2, false);
                  }
               }
            }
         }
      );
   }

   private enum Mode implements TextProvider {
      Cancel("Removes flags and edits rotation with synchronization"),
      Silent("Edits the packet in a way where it is local rotation"),
      Grim("Removes flags and edits rotation and does an additional rotation packet"),
      Flags("Removes X_ROT and Y_ROT flags from the packet");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Mode(final String description) {
         this.text = description;
      }
   }
}
