package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.TimeGate;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.client.MediaModule;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.hud.ClockTime;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.StateFactory;
import org.phobos.core.hud.TextHudElement;
import org.phobos.core.user.UserSession;

public class WelcomerHudElement extends TextHudElement {
   public final TimeGate.Normal normal;
   public final Setting<WelcomerHudElement.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", WelcomerHudElement.Mode.Name));
   public final ClockTime clockTime;
   public final Setting<Boolean> time = this.resolveSetting2(new BooleanSetting("Time"));

   public WelcomerHudElement() {
      super("Welcomer");
      this.normal = new TimeGate.Normal();
      this.clockTime = StateFactory.resolveClockTime(true);
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (WelcomerHudElement.this.normal.testLong2(5000L)) {
                  WelcomerHudElement.this.clockTime.run();
               }
            }
         }
      );
   }

   public String getMessage() {
      return "Good " + switch (this.clockTime.getMorningAfternoonEnum()) {
         case Evening -> "morning";
         case Afternoon -> "evening";
         case Morning -> "afternoon";
         case Night -> "night";
      };
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.player != null) {
         String text = switch ((WelcomerHudElement.Mode)this.mode.getValue()) {
            case Name -> MediaModule.getMediaModule().check7() ? MediaModule.describe2() : mc.player.getNameForScoreboard();
            case User -> UserSession.userSession.getText();
            case UID -> "uid " + UserSession.userSession.getUserProfile().getIntValue();
         };
         String text2 = this.time.getValue() ? this.getMessage() + " " : "Hello ";
         String text3 = this.describeText2(text2, text);
         this.handleDrawContext9(drawContext, text3);
      }
   }

   private enum Mode {
      Name,
      User,
      UID;
   }
}
