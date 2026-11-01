package org.phobos.core.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.core.render.ColoredRect;

public class BackgroundEffects {
   public static BackgroundEffects backgroundEffects = new BackgroundEffects();
   public final List<ColoredRect> coloredRectList = new ArrayList<>();

   public void handleMatrixStack(MatrixStack matrixStack) {
      this.coloredRectList.forEach(coloredRect -> coloredRect.handleMatrixStack(matrixStack));
   }

   public BackgroundEffects() {
      Random random = new Random();

      for (int i2 = 0; i2 < 100; i2++) {
         for (int i = 0; i < 3; i++) {
            ColoredRect coloredRect = new ColoredRect(25 * i2, i * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
            this.coloredRectList.add(coloredRect);
         }
      }
   }
}
