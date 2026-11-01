package org.phobos.core.animation;

import java.awt.Color;

public class AnimatedColor {
   public final AnimatedDouble animatedDouble;
   public final AnimatedDouble animatedDouble2 = new AnimatedDouble();
   public final AnimatedDouble animatedDouble3;
   public final AnimatedDouble animatedDouble4 = new AnimatedDouble();

   public void handleColor(Color color) {
      this.animatedDouble2.handleDouble3(color.getRed() / 255.0F);
      this.animatedDouble4.handleDouble3(color.getGreen() / 255.0F);
      this.animatedDouble3.handleDouble3(color.getBlue() / 255.0F);
      this.animatedDouble.handleDouble3(color.getAlpha() / 255.0F);
   }

   public Color createColor() {
      return new Color(
         (float)this.animatedDouble2.value(), (float)this.animatedDouble4.value(), (float)this.animatedDouble3.value(), (float)this.animatedDouble.value()
      );
   }

   public void handleColor2(Color color) {
      this.animatedDouble2.handleDouble4(color.getRed() / 255.0F);
      this.animatedDouble4.handleDouble4(color.getGreen() / 255.0F);
      this.animatedDouble3.handleDouble4(color.getBlue() / 255.0F);
      this.animatedDouble.handleDouble4(color.getAlpha() / 255.0F);
   }

   public void handleFloat(float f, float f2) {
      this.animatedDouble2.handleFloat(f, f2);
      this.animatedDouble4.handleFloat(f, f2);
      this.animatedDouble3.handleFloat(f, f2);
      this.animatedDouble.handleFloat(f, f2);
   }

   public void handleColor3(Color color) {
      this.animatedDouble2.handleDouble(color.getRed() / 255.0F);
      this.animatedDouble4.handleDouble(color.getGreen() / 255.0F);
      this.animatedDouble3.handleDouble(color.getBlue() / 255.0F);
      this.animatedDouble.handleDouble(color.getAlpha() / 255.0F);
   }

   public void run() {
      this.animatedDouble2.run();
      this.animatedDouble4.run();
      this.animatedDouble3.run();
      this.animatedDouble.run();
   }

   public AnimatedColor() {
      this.animatedDouble3 = new AnimatedDouble();
      this.animatedDouble = new AnimatedDouble();
   }
}
