package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.hud.HudElement;

public class EmptyHudElement013 extends HudElement {
   public final FloatAnimator floatAnimator = new FloatAnimator();

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
   }

   public EmptyHudElement013() {
      super("Debug", 0.0F, 0.0F, 0.0F, 10.0F);
   }
}
