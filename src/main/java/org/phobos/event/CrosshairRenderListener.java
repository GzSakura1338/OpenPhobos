package org.phobos.event;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import org.phobos.core.render.RenderUtils;
import org.phobos.module.render.CrosshairModule;
import org.phobos.module.render.CrosshairModule;

public final class CrosshairRenderListener extends ModuleListener<CrosshairModule, RenderEvent2> {
   public void handleObject2(RenderEvent2 renderEvent2) {
      this.module.floatAnimator.handleFloat3(10.0F, 0.1F);
      this.module.floatAnimator2.handleFloat3(10.0F, 0.08F);
      if (mc.options.getPerspective().isFirstPerson()) {
         Color color = this.module.color.getValue();
         DrawContext drawContext = renderEvent2.getDrawContext();
         int i = mc.getWindow().getScaledWidth();
         int i2 = mc.getWindow().getScaledHeight();
         switch ((CrosshairModule.Style)this.module.style.getValue()) {
            case Default:
               double d = (this.module.animate.getValue() ? this.module.floatAnimator.getFloatValue2() : 0.0F) + this.module.shift.getValue();
               float f = this.module.length.getValue();
               double d2 = i / 2.0;
               double d3 = i2 / 2.0;
               RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color, d2, d3 + d, d2, d3 + d + f);
               RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color, d2, d3 - d + 1.0, d2, d3 - d - f);
               RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color, d2 + d - 1.0, d3, d2 + d + f, d3);
               RenderUtils.handleMatrixStack23(drawContext.getMatrices(), color, d2 - d, d3, d2 - d - f, d3);
               break;
            case Dot:
               double d4 = i / 2.0;
               double d5 = i2 / 2.0;
               double d6 = d4 - 1.0;
               double d7 = d5 + 1.0;
               RenderUtils.handleMatrixStack20(drawContext.getMatrices(), color, d4, d5, d6, d7);
         }

         if (this.module.hit.getValue()) {
            this.module
               .hitCrosshair
               .handleDrawContext(drawContext, this.module.hitColor.getValue(), this.module.floatAnimator2.getFloatValue2(), this.module.hitLength.getValue());
         }
      }
   }

   public CrosshairRenderListener(CrosshairModule crosshairModule) {
      super(crosshairModule, RenderEvent2.class);
   }
}
