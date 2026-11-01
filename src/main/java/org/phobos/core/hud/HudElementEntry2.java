package org.phobos.core.hud;

import net.minecraft.client.util.math.MatrixStack;
import org.phobos.api.HudRenderable;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.render.RenderUtils;

public class HudElementEntry2 implements HudRenderable {
   public final HudElement hudElement;
   public final FloatAnimator floatAnimator = new FloatAnimator(1.0F, 0.0F);
   public final double doubleValue;

   public double getDoubleValue() {
      return this.doubleValue;
   }

   @Override
   public void handleMatrixStack(MatrixStack matrixStack, float f, float f2) {
      this.floatAnimator.handleFloat3(10.0F, 0.06F);
      RenderUtils.handleMatrixStack19(matrixStack, this.getColor(), 0.0, this.doubleValue, f, 0.5F);
   }

   public HudElement getHudElement() {
      return this.hudElement;
   }

   public HudElementEntry2(HudElement hudElement, double d) {
      this.hudElement = hudElement;
      this.doubleValue = d;
   }

   @Override
   public FloatAnimator getFloatAnimator() {
      return this.floatAnimator;
   }
}
