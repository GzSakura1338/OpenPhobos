package org.phobos.api;

import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.render.ColorUtils;
import org.phobos.module.client.ColorsModule;

public interface HudRenderable {
   FloatAnimator getFloatAnimator();

   void handleMatrixStack(MatrixStack var1, float var2, float var3);

   default Color getColor() {
      return ColorUtils.createColor6(ColorsModule.getColorsModule().color2.getValue(), this.getFloatAnimator().getFloatValue2());
   }
}
