package org.phobos.core.hud.element;

import net.minecraft.client.gui.DrawContext;
import org.phobos.core.hud.TextHudElement;

public class MemoryHudElement extends TextHudElement {
   public MemoryHudElement() {
      super("Memory");
   }

   public long computeLong(long l) {
      return l / 1024L / 1024L;
   }

   @Override
   public void handleDrawContext2(DrawContext drawContext, int i, int i2) {
      long l = Runtime.getRuntime().maxMemory();
      long l2 = Runtime.getRuntime().totalMemory();
      long l3 = Runtime.getRuntime().freeMemory();
      long l4 = l2 - l3;
      String text = this.describeText2("RAM: ", this.computeLong(l4) + "/" + this.computeLong(l)) + " " + this.describeText3(l4 * 100L / l + "%");
      this.handleDrawContext9(drawContext, text);
   }
}
