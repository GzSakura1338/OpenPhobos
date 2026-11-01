package org.phobos.core.hud;

import net.minecraft.client.util.math.MatrixStack;
import org.phobos.api.HudRenderable;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.render.RenderUtils;

public class HudElementEntry implements HudRenderable {
   public final double doubleValue;
   public final HudElement hudElement;
   public final FloatAnimator floatAnimator = new FloatAnimator(1.0F, 0.0F);

   public HudElement getHudElement() {
      return this.hudElement;
   }

   @Override
   public void handleMatrixStack(MatrixStack matrixStack, float f, float f2) {
      this.floatAnimator.handleFloat3(10.0F, 0.06F);
      RenderUtils.handleMatrixStack16(matrixStack, this.getColor(), this.doubleValue, 0.0, f2, 0.5F);
   }

   public HudElementEntry(HudElement hudElement, double d) {
      this.hudElement = hudElement;
      this.doubleValue = d;
   }

   @Override
   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }
}
