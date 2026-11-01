package org.phobos.module.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.FlagDetectorReceiveCommonPingListener;
import org.phobos.event.FlagDetectorReceivePlayerPositionLookListener;
import org.phobos.event.FlagDetectorPlayerMoveListener;
import org.phobos.event.FlagDetectorSendPlayerInteractEntityListener;
import org.phobos.module.Module;
import org.phobos.module.client.NotificationsModule;

public class FlagDetectorModule extends Module {
   public int intValue;
   public final Setting<Boolean> combat = new BooleanSetting("Combat", true).resolveSetting6(this);
   public int intValue2;
   public volatile int intValue3;
   public final Setting<Boolean> movement = new BooleanSetting("Movement", true).resolveSetting6(this);
   public boolean flag;
   public PlayerEntity playerEntity;
   public int intValue4;

   public void run() {
      this.handleText("Combat", ++this.intValue4);
   }

   @Override
   public String nothing() {
      return this.combat.getValue() && this.movement.getValue() ? "Shared" : (this.combat.getValue() ? "Combat" : "Movement");
   }

   @Override
   public void run3() {
      this.intValue4 = 0;
      this.intValue = 0;
      this.flag = false;
      this.playerEntity = null;
      this.intValue2 = 0;
   }

   public FlagDetectorModule(PhobosClient phobosClient) {
      super(phobosClient, "FlagDetector", Categories.misc, "Detects when you are flagging the anticheat");
      this.handleListener(new FlagDetectorSendPlayerInteractEntityListener(this));
      this.handleListener(new FlagDetectorReceivePlayerPositionLookListener(this));
      this.handleListener(new FlagDetectorPlayerMoveListener(this));
      this.handleListener(new FlagDetectorReceiveCommonPingListener(this));
   }

   public void handleText(String text, int i) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> {
               Text text2 = Text.literal(text)
                  .withColor(color2.hashCode())
                  .append(Text.literal(" (VL:%s)".formatted(i)).withColor(color3.hashCode()))
                  .append(Text.literal(" -> ").withColor(color2.hashCode()))
                  .append(Text.literal(String.valueOf(this.intValue3)).withColor(color3.hashCode()));
               this.handleText7(text2);
            }
         );
   }

   public void run2() {
      this.handleText("Movement", ++this.intValue);
   }
}
