package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.event.DisconnectEvent;
import org.phobos.event.SafeListener;
import org.phobos.type.CenterRightEnum;
import org.phobos.type.TopCenterEnum;
import org.phobos.core.animation.AnimatedDouble;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.hud.HudAnchor;
import org.phobos.core.setting.Setting;
import org.phobos.core.hud.TextHudElement;

public class CoordsHudElement extends TextHudElement {
   public final Setting<Boolean> dynamic = this.resolveSetting2(new BooleanSetting("Dynamic", false));
   public final AnimatedDouble animatedDouble;
   public final AnimatedDouble animatedDouble2;
   public final Setting<Boolean> nether = this.resolveSetting2(new BooleanSetting("Nether", true).resolveSetting7("Nether coordinates conversion"));
   public boolean flag;
   public final AnimatedDouble animatedDouble3;

   @Override
   public void run7() {
      this.setHudAnchor(
         HudAnchor.createHudAnchor(() -> 0.0F, () -> mc.getWindow().getScaledHeight() - 2.0F, TopCenterEnum.Bottom, CenterRightEnum.Left)
      );
      this.run9();
   }

   public CoordsHudElement() {
      super("Coords");
      this.animatedDouble2 = new AnimatedDouble();
      this.animatedDouble3 = new AnimatedDouble();
      this.animatedDouble = new AnimatedDouble();
      this.flag = false;
      this.handleListener(
         new SafeListener<DisconnectEvent>() {
            public void handleObject(
               DisconnectEvent disconnectEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               CoordsHudElement.this.flag = true;
            }
         }
      );
   }

   public String describeDouble(double d, double d2, double d3) {
      String text = Math.round(d / 8.0) + ", " + Math.round(d2) + ", " + Math.round(d3 / 8.0);
      return " " + this.describeText3(text);
   }

   public String describeDouble2(double d, double d2, double d3) {
      String text = Math.round(d * 8.0) + ", " + Math.round(d2) + ", " + Math.round(d3 * 8.0);
      return " " + this.describeText3(text);
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.player != null && mc.world != null) {
         this.animatedDouble2.run();
         this.animatedDouble3.run();
         this.animatedDouble.run();
         if (this.dynamic.getValue()) {
            this.animatedDouble2.handleDouble4(mc.player.getX());
            this.animatedDouble3.handleDouble4(mc.player.getY());
            this.animatedDouble.handleDouble4(mc.player.getZ());
            if (this.flag) {
               this.flag = false;
               this.animatedDouble2.handleDouble(mc.player.getX());
               this.animatedDouble3.handleDouble(mc.player.getY());
               this.animatedDouble.handleDouble(mc.player.getZ());
            }
         }

         double d = this.dynamic.getValue() ? this.animatedDouble2.value() : mc.player.getX();
         double d2 = this.dynamic.getValue() ? this.animatedDouble3.value() : mc.player.getY();
         double d3 = this.dynamic.getValue() ? this.animatedDouble.value() : mc.player.getZ();
         String text = this.describeText2(
            "XYZ: ",
            Math.round(d)
               + ", "
               + Math.round(d2)
               + ", "
               + Math.round(d3)
               + (
                  this.nether.getValue()
                     ? (mc.world.getDimension().respawnAnchorWorks() ? this.describeDouble2(d, d2, d3) : this.describeDouble(d, d2, d3))
                     : ""
               )
         );
         this.handleDrawContext9(drawContext, text);
      }
   }
}
