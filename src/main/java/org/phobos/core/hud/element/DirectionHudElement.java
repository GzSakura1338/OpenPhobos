package org.phobos.core.hud.element;

import java.util.Objects;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.hud.TextHudElement;

public class DirectionHudElement extends TextHudElement {
   public DirectionHudElement() {
      super("Direction");
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      if (mc.player != null && mc.world != null) {
         Entity entity = Objects.requireNonNullElse(mc.getCameraEntity(), mc.player);

         String text = switch (entity.getHorizontalFacing()) {
            case EAST -> "East";
            case WEST -> "West";
            case NORTH -> "North";
            case SOUTH -> "South";
            default -> "Unknown";
         };
         String text2 = this.describeFloat(MathHelper.wrapDegrees(entity.getYaw()));
         String text3 = this.describeText2("%s ".formatted(text), this.describeText3(text2));
         this.handleDrawContext9(drawContext, text3);
      }
   }

   public String describeFloat(float f) {
      return switch (Math.round(f / 45.0F) * 45) {
         case -180, 180 -> "-Z";
         case -135 -> "+X, -Z";
         case -90 -> "+X";
         case -45 -> "+X, +Z";
         case 0 -> "+Z";
         case 45 -> "-X, +Z";
         case 90 -> "-X";
         case 135 -> "-X, -Z";
         default -> "Unknown";
      };
   }
}
