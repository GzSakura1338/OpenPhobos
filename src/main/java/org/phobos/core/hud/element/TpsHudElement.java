package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.math.MathUtils;
import org.phobos.core.hud.TextHudElement;

public class TpsHudElement extends TextHudElement {
   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      String text = this.describeText2("TPS: ", String.valueOf(MathUtils.computeFloat5(this.phobosClient.getTpsTracker().getFloatValue(), 2)))
         + " "
         + this.describeText3(String.valueOf(MathUtils.computeFloat5(this.phobosClient.getTpsTracker().getFloatValue2(), 2)));
      this.handleDrawContext9(drawContext, text);
   }

   public TpsHudElement() {
      super("TPS");
   }
}
